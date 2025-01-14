package com.restaurant.routes.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CMSMenuItemPUTReq {

    private String name;

    private Integer price;

    private Optional<String> id;

};
