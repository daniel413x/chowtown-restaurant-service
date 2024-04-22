package com.restaurant.routes.cms.dto;

import com.restaurant.model.MenuItem;
import com.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CMSRestaurantDto {

    public static Mono<CMSRestaurantDto> fromRestaurant(Restaurant restaurant) {
        CMSRestaurantDto dto = new CMSRestaurantDto();
        dto.setId(restaurant.getId());
        dto.setUserId(restaurant.getUserId());
        dto.setCountry(restaurant.getCountry());
        dto.setRestaurantName(restaurant.getRestaurantName());
        dto.setCity(restaurant.getCity());
        dto.setDeliveryPrice(restaurant.getDeliveryPrice());
        dto.setCuisines(restaurant.getCuisines());
        dto.setEstimatedDeliveryTime(restaurant.getEstimatedDeliveryTime());
        dto.setImageUrl(restaurant.getImageUrl());
        dto.setIsActivatedByUser(restaurant.getIsActivatedByUser());
        dto.setSlug(restaurant.getSlug());
        return Flux.fromIterable(restaurant.getMenuItems())
                .map(menuItem -> new CMSMenuItemDto(
                        menuItem.getId().toString(),
                        menuItem.getName(),
                        menuItem.getPrice()
                ))
                .collectList()
                .map(menuItems -> {
                    dto.setMenuItems(menuItems);
                    return dto;
                });
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