package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Employee;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    @Query("SELECT * \n" +
            "FROM EMPLOYEE as e\n" +
            "WHERE e.userName LIKE :userName;")
    public ArrayList<Employee> findByUserName(@Param("userName")String userName);
}
