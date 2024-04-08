package com.restaurant.repository;

import com.restaurant.model.Restaurant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RestaurantRepository extends ReactiveMongoRepository<Restaurant, String> {
    Mono<Restaurant> findByUserId(String userId);
}
