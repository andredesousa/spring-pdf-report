package app.service;

import static app.controller.ReportController.mapToOutputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.itextpdf.text.pdf.PdfReader;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@DisplayName("ReportService")
@ExtendWith(MockitoExtension.class)
public class ReportServiceTests {

    @Mock
    transient SpringTemplateEngine templateEngine;

    @InjectMocks
    transient ReportService reportService;

    final String HTML = "<html lang=\"en\"><body>List of Products</body></html>";

    @Test
    @DisplayName("#write returns a pdf")
    void write() {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(HTML);

        assertThat(reportService.write(List.of())).isInstanceOf(ITextRenderer.class);
    }

    @Test
    @DisplayName("#read returns a list of strings")
    void read() throws Exception {
        ITextRenderer document = new ITextRenderer();
        document.setDocumentFromString(HTML);
        document.layout();

        byte[] byteArray = mapToOutputStream(document).toByteArray();
        PdfReader reader = new PdfReader(byteArray);

        assertThat(reportService.read(reader)).isEqualTo(List.of("List of Products"));
    }
}