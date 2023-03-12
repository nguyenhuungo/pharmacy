package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.model.Account;

public interface CardService {
    String addToCard(String id, int quantity);
    void removeItemCard(String id);
    double updateQuantity(String ids, int quantity);
}
