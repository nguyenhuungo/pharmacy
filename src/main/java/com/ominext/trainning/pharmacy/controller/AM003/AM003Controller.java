package com.ominext.trainning.pharmacy.controller.AM003;

import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.request.ProductRegistrationDto;
import com.ominext.trainning.pharmacy.service.ProductService;
import com.ominext.trainning.pharmacy.service.excel.ExcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class AM003Controller {
    private final ProductService productService;
    private final ExcelService excelService;

    public AM003Controller(ProductService productService, ExcelService excelService) {
        this.productService = productService;
        this.excelService = excelService;
    }

    @GetMapping("/AM003/products")
    public String showProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("listProducts", products);
        return "admin/dashboard";
    }

    @GetMapping("/AM003/add_product")
    public String showRegistrationForm() {
        return "admin/AM003/AM003_02";
    }

    @PostMapping("/AM003/add_product")
    public String addNewAccount(ProductRegistrationDto productRegistrationDto) {
        productService.save(productRegistrationDto);
        return "redirect:/AM003/products";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("product") ProductRegistrationDto registrationDto) {
        System.out.println(registrationDto.getId());
//       Update nếu có id, add nếu không có id truyền vào.
        this.productService.save(registrationDto);
        return "redirect:/admin/products";
    }

    @PostMapping("/AM003/import_file")
    public String importFile(@RequestParam("file") MultipartFile file) {
        this.excelService.importExcel(file);
        System.out.println(file);
        return "redirect:/AMO003/products";
    }

    @GetMapping("/AM003/export_file")
    public String exportFile(HttpServletResponse response) throws IOException {
        this.excelService.exportExcel(response);
        return "redirect:/AMO003/products";
    }

    @GetMapping("/delete/{id}")
    public String update(@PathVariable("id") Long id) {
        this.productService.delete(id);
        return "redirect:/admin/products";
    }
}
