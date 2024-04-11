package com.restaurant.routes.cms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CMSRestaurantPUTReq {

    @NotBlank(message = "String \"restaurantName\" must not be empty")
    private String restaurantName;

    @NotBlank(message = "String \"city\" must not be empty")
    private String city;

    @NotBlank(message = "String \"country\" must not be empty")
    private String country;

    @Min(value = 0)
    private Integer deliveryPrice;

    @Min(value = 0)
    private Integer estimatedDeliveryTime;

    @NotEmpty(message = "String list \"cuisines\" cannot be empty")
    private List<String> cuisines;

    @NotBlank(message = "String \"imageUrl\" must not be empty")
    private String imageUrl;

    @NotNull(message = "Boolean \"isActivatedByUser\" must not be null")
    private Boolean isActivatedByUser;

    @NotEmpty(message = "Menu items list \"menuItems\" cannot be empty")
    private List<CMSMenuItemPUTReq> menuItems;
};
