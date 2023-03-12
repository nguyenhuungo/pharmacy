package com.ominext.trainning.pharmacy.controller;

import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.request.ProductRegistrationDto;
import com.ominext.trainning.pharmacy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping()
    public String getAllProduct(@ModelAttribute("product") ProductRegistrationDto registrationDto, Model model) {
        List<Product> productList = this.productService.findAll();
        model.addAttribute("productList", productList);
        return "product";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String ids, @ModelAttribute("product") ProductRegistrationDto registrationDto, Model model) {
        Product product = this.productService.findProductById(ids);
        model.addAttribute("product", product);
        return "update_product";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("product") ProductRegistrationDto registrationDto) {
        System.out.println(registrationDto.getId());
//       Update nếu có id, add nếu không có id truyền vào.
        this.productService.save(registrationDto);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String update(@PathVariable("id") Long ids) {
        this.productService.delete(ids);
        return "redirect:/admin/products";
    }
}
