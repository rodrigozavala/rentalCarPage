package com.project.rentalCarPage.tables;

import org.springframework.data.repository.CrudRepository;
import com.project.rentalCarPage.tables.Car;

public interface CarRepository extends CrudRepository<Car,Integer> {

}
