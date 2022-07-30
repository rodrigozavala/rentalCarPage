package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Integer>{

}
