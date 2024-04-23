package com.app.psa;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PDFController {

	public static final String GET_PDF = "pdf";
	
	private final PDFService pdfService;
	
	PDFController(PDFService pdfService) {
		this.pdfService = pdfService;
	}
	
	
	@RestController
	public class APIController {


	    @GetMapping(value = GET_PDF, produces = MediaType.TEXT_HTML_VALUE)
	    public String getInstitutions() {
	    	try { 
	    		return pdfService.getPDF();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	return null;
	    }
	}
}
