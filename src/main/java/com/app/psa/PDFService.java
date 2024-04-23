package com.app.psa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Service;
import org.ujac.print.DocumentPrinter;
import org.ujac.util.io.ClassPathResourceLoader;


@Service
public class PDFService {

	/*private final Resource resource;
	
	public PDFService(@Value("${invoice.template.path}") Resource resource) {
		resource = new File("").get;
		this.resource = resource;
	}*/
    
    public String getPDF() throws Exception {
    	
    	File initialFile = new File("src\\main\\resources\\templates\\invoice\\template.xml");
        InputStream targetStream = new FileInputStream(initialFile);
    	
    	try (InputStream templateStream = targetStream;
                OutputStream output = new FileOutputStream("C:\\temp\\20240418\\1.pdf")) {

            DocumentPrinter documentPrinter = prepareUjacDocumentPrinter(
                    templateStream, getTemplateData());

            documentPrinter.printDocument(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	return "PDF Generated";
    }
    
    
    public DocumentPrinter prepareUjacDocumentPrinter(InputStream templateStream,
            Map<String, Object> templateData) throws IOException {

    	DocumentPrinter documentPrinter = new DocumentPrinter(templateStream, templateData);
    	documentPrinter.setResourceLoader(new ClassPathResourceLoader(PDFService.class.getClassLoader()));
    	documentPrinter.setTranslateEscapeSequences(false);

    	return documentPrinter;
    }
    
    private Map<String, Object> getTemplateData() {
    	Map<String, Object> templateData = new HashMap<>();
    	templateData.put("companyName", "Hammerstar Wood Workss");
    	byte[] logoContent = getLogoContent();
        templateData.put("headerLogo", logoContent);
    	return templateData;
    }
    
    public byte[] getLogoContent() {
        String imageUrl = "src\\main\\resources\\templates\\invoice\\logo.png";

        try {
        	File initialFile = new File(imageUrl);
        	InputStream targetStream = new FileInputStream(initialFile);
        	return targetStream.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
