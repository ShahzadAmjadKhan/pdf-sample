package com.app.psa;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.ujac.print.DocumentPrinter;
import org.ujac.util.io.ClassPathResourceLoader;


@Service
public class PDFService {

	private final Resource resource;
	
	private static final SimpleDateFormat filenameDate = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public PDFService(@Value("${invoice.template.path}") Resource resource) {
		this.resource = resource;
		
	}
    
    public byte[] getPDF() throws Exception {
    	
    	String outputFileName = getOutputDir();
        byte[] writtenData = null;
    	try (InputStream templateStream = resource.getInputStream();
                OutputStream output = new FileOutputStream(outputFileName)) {

            DocumentPrinter documentPrinter = prepareUjacDocumentPrinter(
                    templateStream, getTemplateData());

            documentPrinter.printDocument(output);
            
            File file = new File(outputFileName);
            writtenData = FileUtils.readFileToByteArray(file);
            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	return writtenData;
    }
    
    public DocumentPrinter prepareUjacDocumentPrinter(InputStream templateStream,
            Map<String, Object> templateData) throws IOException {

    	DocumentPrinter documentPrinter = new DocumentPrinter(templateStream, templateData);
    	documentPrinter.setResourceLoader(new ClassPathResourceLoader(PDFService.class.getClassLoader()));
    	documentPrinter.setTranslateEscapeSequences(false);

    	return documentPrinter;
    }
    
    private String getOutputDir() {
    	String parentDirectory = System.getProperty("java.io.tmpdir") + File.separator;
    	String outputFileName = parentDirectory+filenameDate.format(new Date())+".pdf";
    	System.out.println(outputFileName);
    	return outputFileName;
    }
    
    private Map<String, Object> getTemplateData() {
    	Map<String, Object> templateData = new HashMap<>();
    	templateData.put("companyName", "Hammerstar Wood Works");
    	byte[] logoContent = getLogoContent();
        templateData.put("headerLogo", logoContent);
        templateData.put("payPeriod", "Pay Period: Nov 1st - 15th(2018)");
        templateData.put("payDate", "Pay Date: Nov 14th 2018");
        templateData.put("entriesList", getEntriesList());
        templateData.put("taxableBenfitsList", getBenefitsList());
        templateData.put("currentNetIncome", getCurrentNetIncome());
        
    	return templateData;
    }
    
    public byte[] getLogoContent() {
        String imageUrl = "src\\main\\resources\\templates\\invoice\\logo.png";
        File initialFile = new File(imageUrl);
        try (InputStream targetStream = new FileInputStream(initialFile);) {
        	return targetStream.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }
    
    private List<Entry> getEntriesList() {
    	List<Entry> entriesList = new ArrayList<Entry>();
    	Entry entry = new Entry(); 
    	entry.setIncome("Regular");
    	entry.setThisPeriod("1,600.00");
    	entry.setYearToDate("37,600.00");
    	entry.setDeductions("CPP");
    	entry.setCurrentTotal("93.96");
    	entry.setYearToDate_("1909.76");
    	entriesList.add(entry);
    	
    	entry = new Entry(); 
    	entry.setIncome("Overtime");
    	entry.setThisPeriod("300.00");
    	entry.setYearToDate("5,400.00");
    	entry.setDeductions("Income Tax");
    	entry.setCurrentTotal("324.57");
    	entry.setYearToDate_("5922.77");
    	entriesList.add(entry);
    	
    	entry = new Entry(); 
    	entry.setIncome("Vacation");
    	entry.setThisPeriod("64.00");
    	entry.setYearToDate("768.00");
    	entriesList.add(entry);
    	
    	entry = new Entry(); 
    	entry.setIncome("Bonus");
    	entry.setThisPeriod("0.00");
    	entry.setYearToDate("500.00");
    	entriesList.add(entry);
    	
    	entry = new Entry(); 
    	entry.setIncome("Stat/Holiday");
    	entry.setThisPeriod("0.00");
    	entry.setYearToDate("220.00");
    	entriesList.add(entry);

    	entry = new Entry(); 
    	entry.setIncome("<b>Total Income</b>");
    	entry.setThisPeriod("<b>1964.00</b>");
    	entry.setYearToDate("<b>44,488.00</b>");
    	entriesList.add(entry);
    	
    	return entriesList;
    }
    
    private List<Benefit> getBenefitsList() {
    	List<Benefit> benefitsList = new ArrayList<Benefit>();
    	Benefit benefit = new Benefit(); 
    	benefit.setName("Non-cash Insurable");
    	benefit.setThisPeriod("80.00");
    	benefit.setYearToDate("10400.00");
    	benefitsList.add(benefit);
    	return benefitsList;
    	
    }
    
    private String getCurrentNetIncome() {
    	return "1511.54";
    	
    }
    
    
}

