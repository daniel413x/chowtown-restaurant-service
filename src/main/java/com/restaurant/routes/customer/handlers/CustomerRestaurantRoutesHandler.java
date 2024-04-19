package com.restaurant.routes.customer.handlers;

import com.restaurant.model.Restaurant;
import com.restaurant.routes.customer.dto.CustomerRestaurantDto;
import com.restaurant.routes.customer.dto.CustomerRestaurantGETReq;
import com.restaurant.routes.customer.repository.CustomerRestaurantRepository;
import com.restaurant.utils.ValidationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomerRestaurantRoutesHandler {

    private CustomerRestaurantRepository customerRestaurantRepository;

    @Autowired
    public CustomerRestaurantRoutesHandler(CustomerRestaurantRepository customerRestaurantRepository, ReactiveJwtDecoder jwtDecoder, ValidationHandler validationHandler) {
        this.customerRestaurantRepository = customerRestaurantRepository;
    }

    public CustomerRestaurantRoutesHandler() {}

    public Mono<ServerResponse> get(ServerRequest req) {
        String searchTerm = req.queryParam("searchTerm").orElse("");
        List<String> selectedCuisines = Arrays.asList(req.queryParam("cuisines").orElse("").split(","));
        String sortBy = req.queryParam("sortBy").orElse("lastUpdated");
        String city = req.pathVariable("city").strip();
        PageRequest pageRequest = this.getPageRequest(req);
        Mono<Long> count = customerRestaurantRepository.findByQuery(city, searchTerm, selectedCuisines, Pageable.unpaged()).count();
        Flux<Restaurant> restaurants = customerRestaurantRepository.findByQuery(city, searchTerm, selectedCuisines, pageRequest);
        return Mono.zip(restaurants.collectList(), count)
                .flatMap(result -> {
                    List<CustomerRestaurantDto> dtos = result.getT1().stream().map(CustomerRestaurantDto::new).collect(Collectors.toList());
                    CustomerRestaurantGETReq response = new CustomerRestaurantGETReq(dtos, pageRequest, result.getT2());
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
    }

    private PageRequest getPageRequest(ServerRequest req) {
        String sortBy = req.queryParam("sortBy").orElse("lastUpdated");
        String sortDirection = req.queryParam("sortDir").orElse("desc");
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Map<String, String> queryParams = req.queryParams().toSingleValueMap();
        int page = Integer.parseInt(queryParams.getOrDefault("page", "1"));
        // client app will submit page fields without mongo's zero-based pagination; ensure the repository works with a zero-based var in the next line
        page = page - 1;
        int size = Integer.parseInt(queryParams.getOrDefault("size", "10"));
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    private Mono<ServerResponse> createErrorResponse(Integer code, String message)  {
        Map<String, String> errorResponse = Map.of("message", message);
        return ServerResponse.status(code).contentType(MediaType.APPLICATION_JSON).bodyValue(errorResponse);
    };
}
