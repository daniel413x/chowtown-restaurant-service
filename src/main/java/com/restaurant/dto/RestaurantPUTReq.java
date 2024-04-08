package com.restaurant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantPUTReq {

    @NotBlank(message = "Restaurant name must not be empty")
    private String restaurantName;

    @NotBlank(message = "City must not be empty")
    private String city;

    @NotBlank(message = "Country must not be empty")
    private String country;

    @Min(value = 0)
    private Integer deliveryPrice;

    @Min(value = 0)
    private Integer estimatedDeliveryTime;

    @NotEmpty(message = "Cuisines list cannot be empty")
    private List<String> cuisines;

    @NotBlank(message = "Image URL must not be empty")
    private String imageUrl;

    @NotEmpty(message = "Menu items cannot be empty")
    private List<MenuItemPUTReq> menuItems;
};
