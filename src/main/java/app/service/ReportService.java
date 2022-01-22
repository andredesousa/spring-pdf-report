package app.service;

import app.model.Product;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.PdfReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class ReportService {

    @Autowired
    SpringTemplateEngine templateEngine;

    /**
     * Creates an PDF file based on product list.
     * @param products - The list of products.
     * @return The PDF file.
     */
    public ITextRenderer write(List<Product> products) {
        Context context = new Context(Locale.ENGLISH, Map.of("products", products));
        String html = templateEngine.process("report", context);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        return renderer;
    }

    /**
     * Creates a list of strings based on PDF content.
     * @param reader - The PDF file.
     * @return The content of each page.
     * @throws IOException
     */
    public List<String> read(PdfReader reader) throws IOException {
        List<String> textFromPages = new ArrayList<>();

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            textFromPages.add(PdfTextExtractor.getTextFromPage(reader, i));
        }

        return textFromPages;
    }
}
