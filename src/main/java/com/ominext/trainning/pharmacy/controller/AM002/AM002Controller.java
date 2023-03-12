package com.ominext.trainning.pharmacy.controller.AM002;

import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.request.AccountRegistrationDto;
import com.ominext.trainning.pharmacy.service.AccountService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AM002Controller {
    private final AccountService userService;

    public AM002Controller(AccountService userService) {
        this.userService = userService;
    }

    @GetMapping("/AM002/dashboard")
    public String dashboard(Model model) {
        List<Account> listUsers = userService.getAllUsers();
        model.addAttribute("listAccounts", listUsers);
        return "admin/dashboard";
    }

    @GetMapping("/AM002/users")
    public String showListUsers(Model model) {
        List<Account> listUsers = userService.getAllUsers();
        model.addAttribute("listAccounts", listUsers);
        return "admin/dashboard";
    }

    @GetMapping("/AM002/filter")
    public String filterAccounts(Model model, @Param("keyword") String keyword) {
        List<Account> listUsers = userService.getUsersFilter(keyword);
        model.addAttribute("listAccounts", listUsers);
        model.addAttribute("keyword", keyword);
        return "admin/dashboard";
    }

    @GetMapping("/AM002/delete/{id}")
        public String deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUser(id);
        return "redirect:/AM002/dashboard";
    }

    @GetMapping("/AM002/registration")
    public String showRegistrationForm() {
        return "admin/AM002/AM002_02";
    }

    @PostMapping("/AM002/registration")
    public String addNewAccount(AccountRegistrationDto registrationDto) {
        userService.save(registrationDto);
        return "redirect:/AM002/users";
    }
}
