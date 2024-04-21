package com.restaurant.routes.cms.dto;

import com.restaurant.model.MenuItem;
import com.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CMSMenuItemDto {

    private String id;

    private String name;

    private Integer price;
};