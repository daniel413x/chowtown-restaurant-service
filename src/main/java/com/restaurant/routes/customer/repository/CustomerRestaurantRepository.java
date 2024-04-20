package com.restaurant.routes.customer.repository;

import com.restaurant.model.Restaurant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface CustomerRestaurantRepository extends ReactiveMongoRepository<Restaurant, String> {
    Mono<Restaurant> findBySlug(String slug);
    Flux<Restaurant> findByCity(String city, Pageable pageable);

    @Query(value = "{ 'city': { $regex: ?0, $options: 'i' }, 'restaurantName': { $regex: ?1, $options: 'i' }, $or : [ { $expr: { $eq: ['?2', '[]'] } } , { cuisines : { $all: ?2 } } ] }")
    Flux<Restaurant> findByQuery(String city, String restaurantName, List<String> cuisines, Pageable pageable);
}
