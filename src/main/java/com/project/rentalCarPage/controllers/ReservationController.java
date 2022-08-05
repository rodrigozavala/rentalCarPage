package com.project.rentalCarPage.controllers;

import com.project.rentalCarPage.tables.JDBCClasses.Repositories.ReservationRepository;
import com.project.rentalCarPage.tables.JDBCClasses.toolsToCustomizeNav;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping(value="/successfull_operation")
    public String showSuccessfullReservation(Model model, HttpServletRequest request, HttpServletResponse response){
        toolsToCustomizeNav.navCustomization(model,request,response);
        return "successfull_operation";
    }
}
