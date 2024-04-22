package com.restaurant.routes.customer.dto;

import com.restaurant.model.Restaurant;
import com.restaurant.routes.cms.dto.CMSMenuItemDto;
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

    public static Mono<CustomerRestaurantGETReq> fromRestaurants(List<Restaurant> restaurants, PageRequest pageRequest, Long count) {
        CustomerRestaurantGETReq dto = new CustomerRestaurantGETReq();
        dto.pagination = new Pagination(pageRequest, count);
        return Flux.fromIterable(restaurants)
                .flatMap(CustomerRestaurantDto::fromRestaurant)
                .collectList()
                .map(rows -> {
                    dto.setRows(rows);
                    return dto;
                });
    }

    private List<CustomerRestaurantDto> rows;

    private Pagination pagination;
};
