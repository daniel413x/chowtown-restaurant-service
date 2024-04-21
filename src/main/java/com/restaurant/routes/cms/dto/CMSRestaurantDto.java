package com.restaurant.routes.cms.dto;

import com.restaurant.model.MenuItem;
import com.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CMSRestaurantDto {

    public CMSRestaurantDto(Restaurant restaurant) {
        this.setId(restaurant.getId());
        this.setUserId(restaurant.getUserId());
        this.setCountry(restaurant.getCountry());
        this.setRestaurantName(restaurant.getRestaurantName());
        this.setCity(restaurant.getCity());
        this.setCountry(restaurant.getCountry());
        this.setDeliveryPrice(restaurant.getDeliveryPrice());
        this.setCuisines(restaurant.getCuisines());
        this.setEstimatedDeliveryTime(restaurant.getEstimatedDeliveryTime());
        this.setImageUrl(restaurant.getImageUrl());
        this.setIsActivatedByUser(restaurant.getIsActivatedByUser());
        this.setSlug(restaurant.getSlug());
        Flux.fromIterable(restaurant.getMenuItems())
                .map(menuItemReq -> new CMSMenuItemDto(
                        "menuItemReq.getId().toString()",
                        menuItemReq.getName(),
                        menuItemReq.getPrice()
                ))
                .collectList()
                .doOnNext(this::setMenuItems);
    }

    private String id;

    private String userId;

    private String restaurantName;

    private String city;

    private String country;

    private Integer deliveryPrice;

    private Integer estimatedDeliveryTime;

    private List<String> cuisines;

    private List<CMSMenuItemDto> menuItems;

    private String imageUrl;

    private String slug;

    private Boolean isActivatedByUser;
};