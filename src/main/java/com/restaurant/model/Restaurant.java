package com.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    @Field("userId")
    private String userId;

    @Field("menuItems")
    private List<MenuItem> menuItems;

    @Indexed(unique = true)
    @Field("restaurantName")
    private String restaurantName;

    @Field("city")
    private String city;

    @Field("country")
    private String country;

    @Field("deliveryPrice")
    private Integer deliveryPrice;

    @Field("estimatedDeliveryTime")
    private Integer estimatedDeliveryTime;

    @Field("cuisines")
    private List<String> cuisines;

    @Field("imageUrl")
    private String imageUrl;

    @Field("lastUpdated")
    private LocalDateTime lastUpdated;
}