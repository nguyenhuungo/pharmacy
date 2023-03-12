package com.ominext.trainning.pharmacy.controller.HO001;

import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.request.SearchProductRequest;
import com.ominext.trainning.pharmacy.service.CardService;
import com.ominext.trainning.pharmacy.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class HO001Controller {

    private final ProductService productService;
    private final CardService cardService;

    public HO001Controller(ProductService productService, CardService cardService) {
        this.productService = productService;
        this.cardService = cardService;
    }

    @GetMapping("/HO001")
    public String home(Model model, @ModelAttribute("searchPro") SearchProductRequest searchProductRequest) {
        List<Product> productList = this.productService.findPopularProducts(6);
        model.addAttribute("productList", productList);
        return "/HO/index";
    }

    @GetMapping("/HO001/store")
    public String store(Model model, @ModelAttribute("searchPro") SearchProductRequest searchProductRequest,
                              @RequestParam(value = "page", defaultValue = "0") Integer page,
                              @RequestParam(value = "size", defaultValue = "6") Integer size) {
        Page<Product> productPage = this.productService.findByCondition(searchProductRequest, PageRequest.of(page, size));
        model.addAttribute("productPage", productPage);
        return "/HO/shop";
    }

    @GetMapping("/HO001/productDetail/{id}")
    public String productDetail(Model model, @PathVariable("id") String productID) {
        Product product = this.productService.findProductById(productID);
        model.addAttribute("products", product);
        return "/HO/product-detail";
    }

    @PostMapping("/HO001/addToCard/{id}")
    public String addToCard(@RequestParam(value = "quantity", required = false) Integer quantity, @PathVariable("id") String productID) {
        this.cardService.addToCard(productID, quantity);
        return "redirect:/HO001";
    }

    @DeleteMapping("/HO001/removeCard/{id}")
    public String removeCard(@PathVariable("id") String productID) {
        this.cardService.removeItemCard(productID);
        return "redirect:/HO001";
    }
}
