package com.ominext.trainning.pharmacy.controller.AM004;

import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.request.AccountRegistrationDto;
import com.ominext.trainning.pharmacy.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AM004Controller {
    private final AccountService userService;

    public AM004Controller(AccountService userService) {
        this.userService = userService;
    }

    @GetMapping("/AM004/update_info/{id}")
    public String showAM004(@PathVariable(value = "id") Long id) {
        Account settingAccount = userService.findAccountById(id);
        return "admin/AM004/update_info";
    }

    @PostMapping("/AM004/update_info")
    public String updateInfoAccount(@ModelAttribute AccountRegistrationDto account) {
        userService.updateUser(account);
        return "redirect:/AM002/users";
    }
}
