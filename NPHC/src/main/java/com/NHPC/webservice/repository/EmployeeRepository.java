package com.NHPC.webservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NHPC.webservice.employee.Employee;

/* The @Repository annotation indicates that the class or interface is completely dedicated to performing 
 * all sorts of CRUD Operations such as Create, update, read, or delete the data from the database.
 * It is a marker for any class that fulfills the role or stereotype of a repository (also known as Data Access Object or DAO) 
 * */
@Repository 
public interface EmployeeRepository extends JpaRepository<Employee, String>{


}
