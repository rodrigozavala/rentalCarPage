package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.tables.Car;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarRepository extends CrudRepository<Car,Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE car SET car.availability = 0 WHERE (car.idcar = :idCar);")
    public int makeUnavailable(@Param("idCar") Integer idCar);

    @Modifying
    @Transactional
    @Query("UPDATE car SET car.availability = 1 WHERE (car.idcar = :idCar);")
    public int makeAvailable(@Param("idCar") Integer idCar);

    @Modifying
    @Transactional
    @Query("UPDATE car SET car.idreservation = :idRes , car.idclient = :idClient WHERE (car.idcar = :idCar);")
    public int setReservationAndClient(@Param("idRes") Integer idRes,
                              @Param("idClient") Integer idClient,
                              @Param("idCar") Integer idCar);




}
