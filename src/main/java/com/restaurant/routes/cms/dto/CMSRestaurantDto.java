package com.restaurant.routes.cms.dto;

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
        this.setMenuItems(restaurant.getMenuItems());
        this.setIsActivatedByUser(restaurant.getIsActivatedByUser());
    }

    private String id;

    private String userId;

    private String restaurantName;

    private String city;

    private String country;

    private Integer deliveryPrice;

    private Integer estimatedDeliveryTime;

    private List<String> cuisines;

    private List<MenuItem> menuItems;

    private String imageUrl;

    private Boolean isActivatedByUser;
};