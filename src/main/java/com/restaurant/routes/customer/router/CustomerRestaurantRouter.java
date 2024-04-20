package com.restaurant.routes.customer.router;

import com.restaurant.routes.customer.handlers.CustomerRestaurantRoutesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class CustomerRestaurantRouter {

    @Bean
    public RouterFunction<ServerResponse> customerRestaurantRouterRoutes(CustomerRestaurantRoutesHandler customerRestaurantRoutesHandler) {
        return RouterFunctions
                .route()
                .nest(RequestPredicates.path("/api/customer"), builder -> {
                    builder.GET("/search/{city}", customerRestaurantRoutesHandler::get);
                    builder.GET("/{restaurantSlug}", customerRestaurantRoutesHandler::getBySlug);
                })
                .build();
    }
}
