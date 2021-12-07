package com.NHPC.webservice.employee;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "employee_table")
public class Employee {

	  @Id
	  @Column(name = "id")
	  @NotEmpty(message = "Missing Id(s). Please enter an Id(s).")
	  /* @NotNull: a constrained CharSequence, Collection, Map, or Array is valid as long as it's not null, but it can be empty.
	   * @NotEmpty: a constrained CharSequence, Collection, Map, or Array is valid as long as it's not null, and its size/length is greater than zero.
	   * @NotBlank: a constrained String is valid as long as it's not null, and the trimmed length is greater than zero.*/
	  private String id;

	  @Column(name = "login")
	  @NotEmpty
	  private String login;

	  @Column(name = "name")
	  @NotEmpty
	  private String name;

	  @Column(name = "salary")
	  @NotEmpty
	  private BigDecimal salary; // **Important! It is needed to use "BigDecimal" data-type for "decimal" MySQL data-type. Link -> https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
	  
	  @Column(name = "start_Date") // Show how putting the column name as startDate is giving unexplained bugs. Perhaps the column names do not except capital letters.
	  @NotEmpty
	  private Date startDate; // Both java.util.Date and java.sql.Date is working fine.

	  public Employee() {
		  
	  }
	  
	  public Employee(String id, String login, String name, BigDecimal salary, Date startDate) {
		  this.id = id;
		  this.login = login;
		  this.name = name;
		  this.salary = salary;
		  this.startDate = startDate;
	  }
	  

	  public String getId() {
		  return id;
	  }

	  public void setId(String id) {
		  this.id = id;
	  }

	  public String getLogin() {
		  return login;
	  }

	  public void setLogin(String login) {
		  this.login = login;
	  }

	  public String getName() {
		  return name;
	  }

	  public void setName(String name) {
		  this.name = name;
	  }

	  public BigDecimal getSalary() {
		  return salary;
	  }

	  public void setSalary(BigDecimal salary) {
		  this.salary = salary;
	  }

	  public Date getStartDate() {
		  return startDate;
	  }

	  public void setStartDate(Date startDate) {
		  this.startDate = startDate;
	  }


	  @Override
	  public String toString() {
		  return "EmployeeData [id=" + id + ", login=" + login + ", name=" + name + ", salary=" + salary + ", startDate=" + startDate + "]";
	  }
	  
}
