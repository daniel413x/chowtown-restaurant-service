package com.restaurant.routes.customer.dto;

import com.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerRestaurantGETReq {

    public CustomerRestaurantGETReq(List<CustomerRestaurantDto> restaurants, Long count) {
        this.rows = restaurants;
        this.count = count;
    }

    private List<CustomerRestaurantDto> rows;

    private Long count;
};
