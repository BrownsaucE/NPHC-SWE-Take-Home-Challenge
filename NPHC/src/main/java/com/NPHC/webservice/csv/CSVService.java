package com.NPHC.webservice.csv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.NHPC.webservice.employee.Employee;
import com.NHPC.webservice.repository.EmployeeRepository;
import com.NPHC.webservice.csv.CSVHelper;


@Service
public class CSVService {
	
	@Autowired
	EmployeeRepository repository;

	public void save(MultipartFile file) {
		try {
			List<Employee> employees = CSVHelper.csvToEmployee(file.getInputStream()); 
			repository.saveAll(employees);
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}
	
	public List<Employee> getAllEmployee() {
	    return repository.findAll(); // Can be improved, findAll() is not good for a huge databases
	}
	
	public List<Employee> getEmployee(){
		double defaultMin = 0;
		double defaultMax = 4000.00;
		BigDecimal minSalary = new BigDecimal(defaultMin);
		BigDecimal maxSalary = new BigDecimal(defaultMax);
		
		// For future improvements
		int offset = 0;
		int limit = 0; 
		
		List<Employee> employeeList = (List<Employee>) repository.findAll();
		List<Employee> filteredEmployeeList = new ArrayList<>();
		
		for(Employee employee : employeeList) {
			if(employee.getSalary().compareTo(minSalary) >= 0 && employee.getSalary().compareTo(maxSalary) <= 0)
				filteredEmployeeList.add(employee);
		}
		return filteredEmployeeList;
	}
	
	// Can be improved
	public Employee getEmployeebyId(String id) {
		List<Employee> employeeList = repository.findAll();
		for(Employee employee : employeeList) {
			if(employee.getId().matches(id))
				return employee;
		}
		
		return repository.getById(id); //This is returning null and I could not solve it, had to resort to findAll().
	}
	
	public Employee saveEmployee(Employee employee) {
		return repository.save(employee);
	}

	public void deleteEmployee(Employee employee) {
		repository.delete(employee);
	}
	 
	public ByteArrayInputStream load() {
		List<Employee> employee = repository.findAll();

		ByteArrayInputStream in = CSVHelper.tutorialsToCSV(employee);
		return in;
	}

  
}

