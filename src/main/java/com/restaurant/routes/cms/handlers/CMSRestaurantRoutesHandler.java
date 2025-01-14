package com.restaurant.routes.cms.handlers;

import com.restaurant.routes.cms.dto.CMSRestaurantDto;
import com.restaurant.routes.cms.dto.CMSRestaurantPUTReq;
import com.restaurant.model.MenuItem;
import com.restaurant.model.Restaurant;
import com.restaurant.routes.cms.repository.CMSRestaurantRepository;
import com.restaurant.utils.ValidationHandler;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CMSRestaurantRoutesHandler {

    private CMSRestaurantRepository CMSRestaurantRepository;
    private ReactiveJwtDecoder jwtDecoder;
    private ValidationHandler validationHandler;

    @Autowired
    public CMSRestaurantRoutesHandler(CMSRestaurantRepository CMSRestaurantRepository, ReactiveJwtDecoder jwtDecoder, ValidationHandler validationHandler) {
        this.CMSRestaurantRepository = CMSRestaurantRepository;
        this.jwtDecoder = jwtDecoder;
        this.validationHandler = validationHandler;
    }

    public CMSRestaurantRoutesHandler() {}

    public Mono<ServerResponse> getByAuth0Id(ServerRequest req) {
        String auth0Id = req.pathVariable("auth0id");
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return CMSRestaurantRepository.findByUserId(auth0Id)
                .flatMap(restaurant -> this.getAuth0IdFromToken(authorizationHeader)
                            .flatMap(decodedAuth0Id -> {
                                if (!decodedAuth0Id.equals(restaurant.getUserId())) {
                                    return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials mismatch"));
                                }
                                return convertToDto(restaurant)
                                        .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto));
                            })
                );
    }

    // create new restaurant with initial placeholder properties
    // this is called in tandem with the user creation endpoint
    public Mono<ServerResponse> create(ServerRequest req) {
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return this.getAuth0IdFromToken(authorizationHeader)
                .flatMap(decodedAuth0Id -> CMSRestaurantRepository.findByUserId(decodedAuth0Id)
                        .flatMap(existingRestaurant -> convertToDto(existingRestaurant)
                                .flatMap(dto -> ServerResponse.ok().bodyValue(dto)))
                        .switchIfEmpty(Mono.defer(() -> {
                            UUID uuid = UUID.randomUUID();
                            String restaurantName = "My Restaurant" + "-" + uuid;
                            Restaurant newRestaurant = new Restaurant();
                            newRestaurant.setUserId(decodedAuth0Id);
                            newRestaurant.setRestaurantName(restaurantName);
                            newRestaurant.setCity("Dallas");
                            newRestaurant.setCountry("United States");
                            newRestaurant.setDeliveryPrice(1000);
                            newRestaurant.setEstimatedDeliveryTime(30);
                            newRestaurant.setIsActivatedByUser(false);
                            newRestaurant.setImageUrl("https://res.cloudinary.com/dbpwbih9m/image/upload/v1712971993/restaurant-form-placeholder_ybgxoi.png");
                            newRestaurant.setCuisines(Arrays.asList("Italian", "Mexican"));
                            newRestaurant.setMenuItems(Arrays.asList(
                                    new MenuItem(new ObjectId(), "Pizza", 1000),
                                    new MenuItem(new ObjectId(), "Tacos", 500)
                            ));
                            newRestaurant.setLastUpdated(LocalDateTime.now());
                            String slug = this.createSlug(restaurantName);
                            newRestaurant.setSlug(slug);
                            return CMSRestaurantRepository.save(newRestaurant)
                                    .flatMap(savedRestaurant -> convertToDto(savedRestaurant)
                                            .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).bodyValue(dto)));
                        }))
                );
    }

    public Mono<ServerResponse> update(ServerRequest req) {
        String auth0Id = req.pathVariable("auth0id");
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return CMSRestaurantRepository.findByUserId(auth0Id)
                .flatMap(restaurant -> this.getAuth0IdFromToken(authorizationHeader)
                        .flatMap(decodedAuth0Id -> {
                            if (!decodedAuth0Id.equals(restaurant.getUserId())) {
                                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Credentials mismatch"));
                            }
                            return req.bodyToMono(CMSRestaurantPUTReq.class)
                                    .flatMap(restaurantPUTReq -> {
                                        this.validationHandler.validate(restaurantPUTReq, "userPutReq");
                                        restaurant.setCuisines(restaurantPUTReq.getCuisines());
                                        restaurant.setRestaurantName(restaurantPUTReq.getRestaurantName());
                                        restaurant.setDeliveryPrice(restaurantPUTReq.getDeliveryPrice());
                                        restaurant.setImageUrl(restaurantPUTReq.getImageUrl());
                                        restaurant.setEstimatedDeliveryTime(restaurantPUTReq.getEstimatedDeliveryTime());
                                        restaurant.setCity(restaurantPUTReq.getCity());
                                        restaurant.setCountry(restaurantPUTReq.getCountry());
                                        restaurant.setIsActivatedByUser(restaurantPUTReq.getIsActivatedByUser());
                                        restaurant.setLastUpdated(LocalDateTime.now());
                                        String slug = this.createSlug(restaurantPUTReq.getRestaurantName());
                                        restaurant.setSlug(slug);
                                        CMSRestaurantRepository.save(restaurant);
                                        return Flux.fromIterable(restaurantPUTReq.getMenuItems())
                                                .map(menuItemReq -> new MenuItem(
                                                        menuItemReq.getId().map(ObjectId::new).orElseGet(ObjectId::new),
                                                        menuItemReq.getName(),
                                                        menuItemReq.getPrice()
                                                ))
                                                .collectList()
                                                .doOnNext(restaurant::setMenuItems)
                                                .then(CMSRestaurantRepository.save(restaurant))
                                                .flatMap(savedRestaurant -> ServerResponse.noContent().build());
                                    });
                        })
                )
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found")));
    }

    private Mono<CMSRestaurantDto> convertToDto(Restaurant restaurant) {
        return CMSRestaurantDto.fromRestaurant(restaurant);
    }

    private Mono<String> getAuth0IdFromToken(String authorizationHeader) {
        String tokenValue = authorizationHeader.replace("Bearer ", "");
        return jwtDecoder.decode(tokenValue)
                .map(j -> j.getClaimAsString("sub"));
    }

    private String createSlug(String restaurantName) {
        if (restaurantName == null) {
            return "";
        }
        String normalized = Normalizer.normalize(restaurantName, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String slug = normalized.replaceAll("[^\\p{Alnum}]+", "-").toLowerCase();
        slug = slug.replaceAll("^-+|-+$", "");
        return slug;
    }

    private Mono<ServerResponse> createErrorResponse(Integer code, String message)  {
        Map<String, String> errorResponse = Map.of("message", message);
        return ServerResponse.status(code).contentType(MediaType.APPLICATION_JSON).bodyValue(errorResponse);
    };
}
