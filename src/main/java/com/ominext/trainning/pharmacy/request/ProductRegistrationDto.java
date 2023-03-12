package com.ominext.trainning.pharmacy.request;

import lombok.Data;

@Data
public class ProductRegistrationDto {
    private Long id;
    private String name;
    private double price;
    private float discount;
    private String img;
    private String content;
    private int quantity;
}
