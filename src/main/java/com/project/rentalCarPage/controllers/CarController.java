package com.project.rentalCarPage.controllers;

import com.project.rentalCarPage.tables.JDBCClasses.Repositories.CarRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.ReservationRepository;
import com.project.rentalCarPage.tables.JDBCClasses.tables.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/car") // This means URL's start with /demo (after Application path)
public class CarController {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping(path = ViewConstants.GET_CALL_PATH_BETWEEN/*"/between"*/)
    public @ResponseBody Iterable<Car> getCarsBetween(@RequestParam String pickUpDate, @RequestParam String returnDate) {
        // This returns a JSON or XML with the users


        List<Car> cars = new ArrayList<Car>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime d1 = LocalDateTime.parse(pickUpDate.replace("_", " "), formatter);
        LocalDateTime d2 = LocalDateTime.parse(returnDate.replace("_", " "), formatter);
        for (Car c : carRepository.findAll()) {
            //LocalDateTime start=LocalDateTime.parse(reservationRepository.findById(c.getIdreservation()).get().getPickupdate(),formatter);
            //LocalDateTime end=LocalDateTime.parse(reservationRepository.findById(c.getIdreservation()).get().getReturndate(),formatter);
            LocalDateTime start = reservationRepository.findById(c.getIdreservation()).get().getPickupdate();
            LocalDateTime end = reservationRepository.findById(c.getIdreservation()).get().getReturndate();
            if (start.isAfter(d2)) {
                cars.add(c);
            }
        }

        return cars;
    }

}
