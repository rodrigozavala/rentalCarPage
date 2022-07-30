package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car,Integer> {

}
