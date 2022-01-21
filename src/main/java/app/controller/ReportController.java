package app.controller;

import app.model.Product;
import app.service.ReportService;
import com.itextpdf.text.pdf.PdfReader;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping(value = "/write")
    public ResponseEntity<byte[]> write(@RequestBody List<Product> products) throws Exception {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Report.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(mapToOutputStream(reportService.write(products)).toByteArray());
    }

    @PostMapping(value = "/read", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> read(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportService.read(new PdfReader(file.getInputStream())));
    }

    public static ByteArrayOutputStream mapToOutputStream(ITextRenderer renderer) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        renderer.createPDF(os);
        renderer.finishPDF();

        return os;
    }
}
