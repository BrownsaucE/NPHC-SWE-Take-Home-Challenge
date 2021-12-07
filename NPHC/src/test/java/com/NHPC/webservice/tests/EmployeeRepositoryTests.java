package com.NHPC.webservice.tests; // Location of this class needs to be at a lower hierarchy to the class that has the @SpringBootApplication annotation

import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import com.NHPC.webservice.ResponseMessage.ResponseMessage;
import com.NHPC.webservice.employee.Employee;
import com.NHPC.webservice.repository.EmployeeRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class EmployeeRepositoryTests {

	@Autowired
	private EmployeeRepository repository;
	
	
	@Test
	public void testGet() {
		String id = "e0002";
		Optional<Employee> employee = repository.findById(id);
		Assertions.assertThat(employee.isPresent());
		System.out.println(employee.get());
	}
	
	@Test
	public void testFailCreate() {
		try {
			Employee employee = new Employee();
			BigDecimal salary =  new BigDecimal(1234.00);
			
			String dateString = "2001-11-16";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    	LocalDate localDate = LocalDate.parse(dateString, formatter);
			Date date = java.sql.Date.valueOf(localDate);
			
			employee.setId("emp0001");
			employee.setLogin("hpotter");
			employee.setName("Harry Potter");
			employee.setSalary(salary);
			employee.setStartDate(date);
			
			Employee updatedData = repository.save(employee);
			Assertions.assertThat(updatedData).isNotNull();
			Assertions.assertThat(updatedData.getId()).isNotEmpty();
			
			fail("Duplicate entry 'hpotter' for key 'employeedata.login_UNIQUE'");
		}
		catch(Exception e) {
			String message = ""; 
			message = "400 - Duplicate entry 'hpotter' for key 'employeedata.login_UNIQUE'";
			Assertions.assertThat(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message,"")));
		}
		
	}
	
	@Test
	public void testSuccessfulCreate() {
		Employee employee = new Employee();
		BigDecimal salary =  new BigDecimal(1234.00);
		
		String dateString = "2001-11-16";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	LocalDate localDate = LocalDate.parse(dateString, formatter);
		Date date = java.sql.Date.valueOf(localDate);
		
		employee.setId("emp0002");
		employee.setLogin("newLogin");
		employee.setName("newName");
		employee.setSalary(salary);
		employee.setStartDate(date);
		
		Employee updatedEmployee = repository.save(employee);
		Assertions.assertThat(updatedEmployee).isNotNull();
		Assertions.assertThat(updatedEmployee.getId()).isNotEmpty();
	}
	
	@Test
	public void testUpdate() {
		String id = "e0001";
		
		/* By using Optional, we can specify alternate values to return or alternate code to run. 
		 * This makes the code more readable because the facts which were hidden are now visible to the developer.
		 */
		Optional<Employee> employeeOptional = repository.findById(id);
		Employee employee = employeeOptional.get();
		employee.setLogin("harrypotter"); // Previous Login was "hpotter"
		repository.save(employee);
		
		Employee updatedEmployee = repository.findById(id).get();
		Assertions.assertThat(updatedEmployee.getLogin()).isEqualTo("harrypotter");
	}
	
	@Test
	public void testDelete() {
		String id = "emp0002"; // Created from testSuccessfulCreate()
		repository.deleteById(id);
		
		Optional<Employee> employee = repository.findById(id);
		Assertions.assertThat(employee).isNotPresent();
	}

}
