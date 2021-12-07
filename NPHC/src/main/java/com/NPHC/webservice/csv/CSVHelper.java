package com.NPHC.webservice.csv;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import com.NHPC.webservice.employee.Employee;


public class CSVHelper {
	
	public static String TYPE = "text/csv";
	static String[] HEADERs = { "id", "login", "name", "salary", "startDate" };

	public static boolean hasCSVFormat(MultipartFile file) {
		System.out.println(file.getContentType());
		if (TYPE.equals(file.getContentType()) || file.getContentType().equals("application/vnd.ms-excel")) {
			return true;
		}
		return false;
	}

    public static List<Employee> csvToEmployee(InputStream is) {
    	try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  
    			CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) { 

        List<Employee> employeeList = new ArrayList<>();
        List<CSVRecord> csvRecords = csvParser.getRecords();
     
        for (CSVRecord csvRecord : csvRecords) {
  
        	/*System.out.println(csvRecord.get("id") +
    				csvRecord.get("login") +
    				csvRecord.get("name") +
    				BigDecimal.valueOf(Double.valueOf(csvRecord.get("salary"))) + 
    				CSVHelper.parseStringtoDate(csvRecord.get("startDate")));*/
        	
    		Employee employee = new Employee(
    				csvRecord.get("id"),
    				csvRecord.get("login"),
    				csvRecord.get("name"),
    				BigDecimal.valueOf(Double.valueOf(csvRecord.get("salary"))), // **Important! This fucking single piece of code took me 7 hours. It is needed to use "BigDecimal" data-type for "decimal" MySQL data-type. Link -> https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
    				CSVHelper.parseStringtoDate(csvRecord.get("startDate"))
    		);
    		
    		//System.out.println(employee.toString());
    		employeeList.add(employee);
        }
        
        // Console print to check the parsed csv data
        /*for(Employee employee : employeeList) {
        	System.out.println(employee.toString());
        }*/
        
        return employeeList;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

    public static ByteArrayInputStream tutorialsToCSV(List<Employee> employeeList) {
    	final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);){
        	for (Employee employee : employeeList) {
                List<String> employeeData = Arrays.asList(
                		employee.getId(),
                		employee.getLogin(),
                		employee.getName(),
	                    String.valueOf(employee.getSalary()),
	                    String.valueOf(employee.getStartDate())
                );

              csvPrinter.printRecord(employeeData);
        }

        csvPrinter.flush();
        return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
        throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }
    
    // Can be improved
    public static Date parseStringtoDate(String date) {
    	Pattern DATE_PATTERN = Pattern.compile("^(([0-9])|([0-2][0-9])|([3][0-1]))\\-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\-\\d{2}$");
    	
    	if(!DATE_PATTERN.matcher(date).matches()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.US);
        	LocalDate localDate = LocalDate.parse(date, formatter);
    		return java.sql.Date.valueOf(localDate);
    	}
    	else {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy").withLocale(Locale.US);
        	LocalDate localDate = LocalDate.parse(date, formatter);
        	return java.sql.Date.valueOf(localDate);
    	}
    }
}

	