package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.QueryJoinReservation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface QueryJoinReservationRepository extends CrudRepository<QueryJoinReservation,Integer> {
    @Query("SELECT c.name, c.lastname, c.email, c.phone, car.idcar, cm.modelname, cm.imagepath, \n" +
            "car.priceperday, cm.kmperl, cm.auttransmission, cm.peoplecapacity, \n" +
            "cm.luggagecapacity, r.idreservation, r.reservationdate, r.pickupdate, \n" +
            "r.returndate, r.validity, car.availability\n" +
            "FROM reservation AS r\n" +
            "LEFT JOIN client AS c\n" +
            "ON c.idclient=r.idclient\n" +
            "JOIN car\n" +
            "ON car.idcar=r.idcar\n" +
            "JOIN carmodel AS cm\n" +
            "ON car.idmodel=cm.idmodel\n" +
            "WHERE c.idclient= :idClient\n" +
            "ORDER BY r.reservationdate DESC;")
    public ArrayList<QueryJoinReservation> findReservationsById(@Param("idClient") Integer idClient);

    @Query("SELECT c.name, c.lastname, c.email, c.phone, car.idcar, cm.modelname, cm.imagepath, \n" +
            "car.priceperday, cm.kmperl, cm.auttransmission, cm.peoplecapacity, \n" +
            "cm.luggagecapacity, r.idreservation, r.reservationdate, r.pickupdate, \n" +
            "r.returndate, r.validity, car.availability\n" +
            "FROM reservation AS r\n" +
            "LEFT JOIN client AS c\n" +
            "ON c.idclient=r.idclient\n" +
            "JOIN car\n" +
            "ON car.idcar=r.idcar\n" +
            "JOIN carmodel AS cm\n" +
            "ON car.idmodel=cm.idmodel\n" +
            "WHERE r.idreservation= :idRes\n" +
            "ORDER BY r.reservationdate DESC;")
    public ArrayList<QueryJoinReservation> findReservationsByResId(@Param("idRes") Integer id);

    @Query("SELECT c.name, c.lastname, c.email, c.phone, car.idcar, cm.modelname, cm.imagepath, \n" +
            "car.priceperday, cm.kmperl, cm.auttransmission, cm.peoplecapacity, \n" +
            "cm.luggagecapacity, r.idreservation, r.reservationdate, r.pickupdate, \n" +
            "r.returndate, r.validity, car.availability\n" +
            "FROM reservation AS r\n" +
            "LEFT JOIN client AS c\n" +
            "ON c.idclient=r.idclient\n" +
            "JOIN car\n" +
            "ON car.idcar=r.idcar\n" +
            "JOIN carmodel AS cm\n" +
            "ON car.idmodel=cm.idmodel\n" +
            "ORDER BY r.reservationdate DESC;")
    public ArrayList<QueryJoinReservation>findAll();

    @Query("SELECT c.name, c.lastname, c.email, c.phone, car.idcar, cm.modelname, cm.imagepath, \n" +
            "car.priceperday, cm.kmperl, cm.auttransmission, cm.peoplecapacity, \n" +
            "cm.luggagecapacity, r.idreservation, r.reservationdate, r.pickupdate, \n" +
            "r.returndate, r.validity, car.availability\n" +
            "FROM car\n" +
            "LEFT JOIN client AS c\n" +
            "ON car.idclient=c.idclient\n" +
            "JOIN carmodel AS cm\n" +
            "ON car.idmodel=cm.idmodel\n" +
            "JOIN reservation AS r\n" +
            "ON r.idreservation=car.idreservation;")
    public ArrayList<QueryJoinReservation> findAllCars();

}
