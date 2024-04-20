package com.restaurant.routes.customer.dto;

import com.restaurant.model.MenuItem;
import com.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRestaurantDto {

    public CustomerRestaurantDto(Restaurant restaurant) {
        this.setId(restaurant.getId());
        this.setCountry(restaurant.getCountry());
        this.setRestaurantName(restaurant.getRestaurantName());
        this.setCity(restaurant.getCity());
        this.setCountry(restaurant.getCountry());
        this.setDeliveryPrice(restaurant.getDeliveryPrice());
        this.setCuisines(restaurant.getCuisines());
        this.setEstimatedDeliveryTime(restaurant.getEstimatedDeliveryTime());
        this.setImageUrl(restaurant.getImageUrl());
        this.setMenuItems(restaurant.getMenuItems());
        this.setSlug(restaurant.getSlug());
    }

    private String id;

    private String restaurantName;

    private String city;

    private String country;

    private Integer deliveryPrice;

    private Integer estimatedDeliveryTime;

    private List<String> cuisines;

    private List<MenuItem> menuItems;

    private String imageUrl;

    private String slug;

    private Boolean isActivatedByUser;
};