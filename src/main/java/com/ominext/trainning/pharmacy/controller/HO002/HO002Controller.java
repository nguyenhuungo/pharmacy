package com.ominext.trainning.pharmacy.controller.HO002;

import com.ominext.trainning.pharmacy.request.AccountRegistrationDto;
import com.ominext.trainning.pharmacy.service.CardService;
import com.ominext.trainning.pharmacy.service.payBillService.PayBillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@Controller
public class HO002Controller {
    private final PayBillService payBillService;
    private final CardService cardService;

    public HO002Controller(PayBillService payBillService, CardService cardService) {
        this.payBillService = payBillService;
        this.cardService = cardService;
    }

    @PostMapping("/HO002/payTheBill")
    public String payTheBill(@ModelAttribute("account") AccountRegistrationDto registrationDto) throws MessagingException {
        this.payBillService.payTheBill(registrationDto);
        return "redirect:/HO001";
    }

    @GetMapping("/HO002/viewOrder")
    public String viewOrder(Model model, @ModelAttribute("account") AccountRegistrationDto registrationDto) {
        if (this.payBillService.view() == null) {
            return "redirect:/HO001";
        } else {
            model.addAttribute("map", this.payBillService.view());
            return "/HO/pay-page";
        }
    }

    @GetMapping("/HO002/removeCard/{id}")
    public String removeCard(@PathVariable("id") String productID) {
        this.cardService.removeItemCard(productID);
        return "redirect:/HO002/viewOrder";
    }

    @PostMapping("/HO002/updateQuantity")
    public @ResponseBody double updateQuantity(@RequestParam("id") String id, @RequestParam("quantity") int quantity) {
        return this.cardService.updateQuantity(id, quantity);
    }
}
