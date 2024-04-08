package com.restaurant.router;

import com.restaurant.handlers.RestaurantRoutesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class RestaurantRouter {

    @Bean
    public RouterFunction<ServerResponse> restaurantRouterRoutes(RestaurantRoutesHandler restaurantRoutesHandler) {
        return RouterFunctions
                .route()
                .nest(RequestPredicates.path("/api"), builder -> {
                    builder.GET("/{auth0id}", restaurantRoutesHandler::getByAuth0Id);
                    builder.POST("", restaurantRoutesHandler::create);
                    builder.PUT("/{auth0id}", restaurantRoutesHandler::update);
                })
                .build();
    }
}
