package com.ominext.trainning.pharmacy.service.excel;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExcelService {
    String importExcel(MultipartFile file);
    String exportExcel(HttpServletResponse response) throws IOException;
}
