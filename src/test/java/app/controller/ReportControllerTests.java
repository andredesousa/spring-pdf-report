package app.controller;

import static app.controller.ReportController.mapToOutputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import app.service.ReportService;
import com.itextpdf.text.pdf.PdfReader;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;

@DisplayName("ReportController")
@ExtendWith(MockitoExtension.class)
public class ReportControllerTests {

    @Mock
    transient ReportService reportService;

    @InjectMocks
    transient ReportController reportController;
    
    transient ITextRenderer document;

    @BeforeEach
    void beforeEach() {
        document = new ITextRenderer();
        document.setDocumentFromString("<html lang=\"en\"></html>");
        document.layout();
    }

    @Test
    @DisplayName("#write returns an PDF file")
    void write() throws Exception {
        when(reportService.write(List.of())).thenReturn(document);

        ResponseEntity<byte[]> actual = reportController.write(List.of());
        ResponseEntity<byte[]> expected = ResponseEntity.ok()
            .contentType(APPLICATION_PDF)
            .header(CONTENT_DISPOSITION, "attachment; filename=Report.pdf")
            .body(actual.getBody());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("#read returns a list of strings")
    void read() throws Exception {
        byte[] byteArray = mapToOutputStream(document).toByteArray();
        MockMultipartFile file = new MockMultipartFile("file", "Report.pdf", MULTIPART_FORM_DATA_VALUE, byteArray);

        when(reportService.read(any(PdfReader.class))).thenReturn(List.of(""));

        ResponseEntity<List<String>> actual = reportController.read(file);
        ResponseEntity<List<String>> expected = ResponseEntity.ok()
            .contentType(APPLICATION_JSON)
            .body(List.of(""));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
