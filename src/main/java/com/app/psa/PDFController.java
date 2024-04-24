package com.app.psa;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PDFController {

	public static final String GET_PDF = "pdf";
	
	private final PDFService pdfService;
	
	PDFController(PDFService pdfService) {
		this.pdfService = pdfService;
	}
	
    @GetMapping(value = GET_PDF, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<byte[]> getPDF() {
    	try { 
    		byte[] bytes = pdfService.getPDF();
    		HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            return response;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }

}
