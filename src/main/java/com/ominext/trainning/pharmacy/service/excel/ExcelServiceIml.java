package com.ominext.trainning.pharmacy.service.excel;

import com.ominext.trainning.pharmacy.helper.ExcelHelper;
import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExcelServiceIml implements ExcelService {

    private final ProductRepository productRepository;

    public ExcelServiceIml(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String importExcel(MultipartFile file) {
        try {
            List<Product> products = ExcelHelper.excelToProduct(file.getInputStream());
            productRepository.saveAll(products);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=product" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Product> productList = this.productRepository.findAllByDeletedIsFalse();

        ExcelHelper.exportExcelFile(response, productList);
        return null;
    }
}
