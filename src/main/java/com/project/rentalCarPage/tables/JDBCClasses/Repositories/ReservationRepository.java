package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.tables.Reservation;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer>{
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reservation (idCar,idClient,validity,reservationDate,pickUpDate,returnDate)\n" +
            "VALUES( :idCar , :idClient , :Validity , :resDate , :pDate, :rDate );")
    public int insertReservation(@Param("idCar")Integer idcar,
                                  @Param("idClient")Integer idclient,
                                  @Param("Validity")Integer validity,
                                  @Param("resDate") String resDate,
                                  @Param("pDate")String pDate,
                                  @Param("rDate")String rDate);

    @Query("SELECT *\n" +
            "FROM reservation as r\n" +
            "WHERE r.idreservation IN (SELECT MAX(reservation.idreservation)\n" +
            "FROM reservation);")
    public ArrayList<Reservation> checkLast();
    @Modifying
    @Transactional
    @Query("UPDATE reservation SET reservation.validity = 0 WHERE (reservation.idReservation = :idRes);")
    public int cancelReservation(@Param("idRes") Integer idRes);

}
