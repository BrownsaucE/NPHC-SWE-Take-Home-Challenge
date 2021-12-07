package com.NPHC.webservice.csv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.NHPC.webservice.ResponseMessage.ResponseMessage;
import com.NHPC.webservice.employee.Employee;

@Controller
public class CSVController {

	@Autowired
	CSVService fileService;
	
	// Upload API
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		
		if(CSVHelper.hasCSVFormat(file)){
			try {
				fileService.save(file);
				
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
		        
		        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		        		.path("/download/")
		                .path(file.getOriginalFilename())
		                .toUriString();

		        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,fileDownloadUri)); // 200 OK
			}catch(Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
		        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message,"")); // 417 Exception Failed
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message,"")); // 400 Bad Request
	}
	
	
	@GetMapping("/allusers")
	public ResponseEntity<List<Employee>> getAllEmployeeData(){
		try {
			List<Employee> employees = fileService.getAllEmployee();
			if(employees.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
			}
			return new ResponseEntity<>(employees, HttpStatus.OK); // 200 OK
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch API
	@GetMapping("/users")
	public ResponseEntity<List<Employee>> getEmployeeData(){
		try {
			List<Employee> employees = fileService.getEmployee();
			if(employees.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
			}
			return new ResponseEntity<>(employees, HttpStatus.OK); // 200 OK
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// https://github.com/RameshMF/spring-boot-tutorial/tree/master/springboot2-jpa-h2-crud-example
	// CRUD API - GET
	@GetMapping("/users/{$id}")
	public ResponseEntity<Employee> getEmployeeDataById(@PathVariable("$id") String id){
		try {
			Employee employee = fileService.getEmployeebyId(id);
			if(employee == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
			}
			return new ResponseEntity<>(employee, HttpStatus.OK); // 200 OK
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// CRUD API - UPDATE
	@PutMapping("/users/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") String id, @Valid @RequestBody Employee employeeData){
		try {
			Employee data = fileService.getEmployeebyId(id);
			if(data == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
			}
			
			data.setId(employeeData.getId());
			data.setLogin(employeeData.getLogin());
			data.setName(employeeData.getName());
			data.setSalary(employeeData.getSalary());
			data.setStartDate(employeeData.getStartDate());
			final Employee updatedEmployeeData = fileService.saveEmployee(data);
			return new ResponseEntity<>(updatedEmployeeData, HttpStatus.OK); // 200 OK
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// CRUD API - DELETE
	@DeleteMapping("/delete/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable("id") String id){
		Employee data = fileService.getEmployeebyId(id);
		Map<String, Boolean> response = new HashMap<>();
		if(data == null) {
			response.put("deleted", Boolean.FALSE);
			return response;
		}
		fileService.deleteEmployee(data);
		response.put("deleted", Boolean.TRUE);
		return response;
	}
	
	// CRUD API - CREATE
	@PostMapping("/users")
	public ResponseEntity<String> createEmployeeData(Employee employeeData){
		String message = "";
		try {
			fileService.saveEmployee(employeeData);
			message = "New employee record created.";
			return ResponseEntity.status(HttpStatus.CREATED).body(message); // 201 Created
		}catch(Exception e) {
			message = "Bad input - see error cases below.";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message); // 400 Bad Request
		}
	} 
	
	
	// Original code
  /*@PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (CSVHelper.hasCSVFormat(file)) {
      try {
        fileService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/csv/download/")
                .path(file.getOriginalFilename())
                .toUriString();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,fileDownloadUri));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message,""));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message,""));
  }*/

	
  /*@GetMapping("/tutorials")
  public ResponseEntity<List<EmployeeData>> getAllTutorials() {
    try {
      List<EmployeeData> tutorials = fileService.getAllTutorials();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/download/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
    InputStreamResource file = new InputStreamResource(fileService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }*/
  
  	
}
