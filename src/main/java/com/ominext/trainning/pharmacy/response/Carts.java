package com.ominext.trainning.pharmacy.response;

import com.ominext.trainning.pharmacy.model.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Carts {
    private Long userId;

    private List<Item> items;

    @Data
    public static class Item {
        private String name;
        private double price;
        private int quantity;
    }
}
