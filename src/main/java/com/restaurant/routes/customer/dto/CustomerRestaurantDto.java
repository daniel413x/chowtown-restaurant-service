package com.restaurant.routes.customer.dto;

import com.restaurant.model.MenuItem;
import com.restaurant.model.Restaurant;
import com.restaurant.routes.cms.dto.CMSMenuItemDto;
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
        this.setSlug(restaurant.getSlug());
        List<CustomerMenuItemDto> menuItems = restaurant.getMenuItems().stream().map(menuItem -> {
            String id = menuItem.getId().toString();
            String name = menuItem.getName();
            Integer price = menuItem.getPrice();
            return new CustomerMenuItemDto(id, name, price);
        }).toList();
        this.setMenuItems(menuItems);
    }

    private String id;

    private String restaurantName;

    private String city;

    private String country;

    private Integer deliveryPrice;

    private Integer estimatedDeliveryTime;

    private List<String> cuisines;

    private List<CustomerMenuItemDto> menuItems;

    private String imageUrl;

    private String slug;

    private Boolean isActivatedByUser;
};