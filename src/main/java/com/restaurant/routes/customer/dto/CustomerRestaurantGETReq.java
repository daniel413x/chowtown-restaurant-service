package com.restaurant.routes.customer.dto;

import com.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerRestaurantGETReq {

    public CustomerRestaurantGETReq(List<CustomerRestaurantDto> restaurants, PageRequest pageRequest, Long count) {
        this.pagination = new Pagination(pageRequest, count);
        this.rows = restaurants;
    }

    private List<CustomerRestaurantDto> rows;

    private Pagination pagination;
};
