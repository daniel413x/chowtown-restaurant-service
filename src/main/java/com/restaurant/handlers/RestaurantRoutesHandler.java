package com.restaurant.handlers;

import com.restaurant.dto.RestaurantDto;
import com.restaurant.dto.RestaurantPUTReq;
import com.restaurant.model.MenuItem;
import com.restaurant.model.Restaurant;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.utils.ValidationHandler;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestaurantRoutesHandler {

    private RestaurantRepository restaurantRepository;
    private ReactiveJwtDecoder jwtDecoder;
    private ValidationHandler validationHandler;

    @Autowired
    public RestaurantRoutesHandler(RestaurantRepository restaurantRepository, ReactiveJwtDecoder jwtDecoder, ValidationHandler validationHandler) {
        this.restaurantRepository = restaurantRepository;
        this.jwtDecoder = jwtDecoder;
        this.validationHandler = validationHandler;
    }

    public RestaurantRoutesHandler() {}

    public Mono<ServerResponse> getByAuth0Id(ServerRequest req) {
        String auth0Id = req.pathVariable("auth0id");
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return restaurantRepository.findByUserId(auth0Id)
                .flatMap(restaurant -> this.getAuth0IdFromToken(authorizationHeader)
                            .flatMap(decodedAuth0Id -> {
                                if (!decodedAuth0Id.equals(restaurant.getUserId())) {
                                    return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials mismatch"));
                                }
                                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(convertToDto(restaurant));
                            })
                );
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return this.getAuth0IdFromToken(authorizationHeader)
                .flatMap(decodedAuth0Id -> restaurantRepository.findByUserId(decodedAuth0Id)
                        .flatMap(existingRestaurant -> ServerResponse.ok().bodyValue(convertToDto(existingRestaurant)))
                        .switchIfEmpty(Mono.defer(() -> {
                            // create new restaurant with initial placeholder properties
                            Restaurant newRestaurant = new Restaurant();
                            newRestaurant.setUserId(decodedAuth0Id);
                            newRestaurant.setRestaurantName("My Restaurant");
                            newRestaurant.setCity("Dallas");
                            newRestaurant.setCountry("United States");
                            newRestaurant.setDeliveryPrice(1000);
                            newRestaurant.setEstimatedDeliveryTime(30);
                            newRestaurant.setImageUrl("https://res.cloudinary.com/dbpwbih9m/image/upload/v1712544445/chowtown/restaurant-form-placeholder_ehhscf.png");
                            newRestaurant.setCuisines(Arrays.asList("Italian", "Mexican"));
                            newRestaurant.setMenuItems(Arrays.asList(
                                    new MenuItem(null, "Pizza", 1000),
                                    new MenuItem(null, "Tacos", 500)
                            ));
                            newRestaurant.setLastUpdated(LocalDateTime.now());
                            return restaurantRepository.save(newRestaurant)
                                    .flatMap(savedRestaurant -> ServerResponse.status(HttpStatus.CREATED).bodyValue(convertToDto(savedRestaurant)));
                        }))
                );
    }

    public Mono<ServerResponse> update(ServerRequest req) {
        String auth0Id = req.pathVariable("auth0id");
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return restaurantRepository.findByUserId(auth0Id)
                .flatMap(restaurant -> this.getAuth0IdFromToken(authorizationHeader)
                        .flatMap(decodedAuth0Id -> {
                            if (!decodedAuth0Id.equals(restaurant.getUserId())) {
                                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials mismatch"));
                            }
                            return req.bodyToMono(RestaurantPUTReq.class)
                                    .flatMap(restaurantPUTReq -> {
                                        this.validationHandler.validate(restaurantPUTReq, "userPutReq");
                                        restaurant.setCuisines(restaurantPUTReq.getCuisines());
                                        restaurant.setRestaurantName(restaurantPUTReq.getRestaurantName());
                                        restaurant.setDeliveryPrice(restaurantPUTReq.getDeliveryPrice());
                                        restaurant.setImageUrl(restaurantPUTReq.getImageUrl());
                                        restaurant.setEstimatedDeliveryTime(restaurantPUTReq.getEstimatedDeliveryTime());
                                        restaurant.setCity(restaurantPUTReq.getCity());
                                        restaurant.setCountry(restaurantPUTReq.getCountry());
                                        restaurant.setMenuItems(restaurantPUTReq.getMenuItems().stream()
                                                .map(menuItemReq -> new MenuItem(null, menuItemReq.getName(), menuItemReq.getPrice()))
                                                .collect(Collectors.toList()));
                                        restaurant.setLastUpdated(LocalDateTime.now());
                                        return restaurantRepository.save(restaurant)
                                                .flatMap(savedRestaurant -> ServerResponse.status(HttpStatus.OK).bodyValue(convertToDto(savedRestaurant)));
                                    });
                        })
                )
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found")));
    }

    private RestaurantDto convertToDto(Restaurant restaurant) {
        return new RestaurantDto(restaurant);
    }

    private Mono<String> getAuth0IdFromToken(String authorizationHeader) {
        String tokenValue = authorizationHeader.replace("Bearer ", "");
        return jwtDecoder.decode(tokenValue)
                .map(j -> j.getClaimAsString("sub"));
    }

    private Mono<ServerResponse> createErrorResponse(Integer code, String message)  {
        Map<String, String> errorResponse = Map.of("message", message);
        return ServerResponse.status(code).contentType(MediaType.APPLICATION_JSON).bodyValue(errorResponse);
    };
}
