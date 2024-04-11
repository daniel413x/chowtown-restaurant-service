package com.restaurant.routes.cms.router;

import com.restaurant.routes.cms.handlers.CMSRestaurantRoutesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class CMSRestaurantRouter {

    @Bean
    public RouterFunction<ServerResponse> cmsRestaurantRouterRoutes(CMSRestaurantRoutesHandler CMSRestaurantRoutesHandler) {
        return RouterFunctions
                .route()
                .nest(RequestPredicates.path("/api/cms"), builder -> {
                    builder.GET("/{auth0id}", CMSRestaurantRoutesHandler::getByAuth0Id);
                    builder.POST("", CMSRestaurantRoutesHandler::create);
                    builder.PUT("/{auth0id}", CMSRestaurantRoutesHandler::update);
                })
                .build();
    }
}
