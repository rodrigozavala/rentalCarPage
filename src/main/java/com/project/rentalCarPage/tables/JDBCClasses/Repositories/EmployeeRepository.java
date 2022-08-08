package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Client;
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

    @Query("SELECT * FROM employee AS e \n" +
            "WHERE e.username LIKE :userName\n" +
            "AND e.password LIKE :password;")
    public ArrayList<Employee> findByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);
}
