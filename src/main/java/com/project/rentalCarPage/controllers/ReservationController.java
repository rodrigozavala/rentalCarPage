package com.project.rentalCarPage.controllers;

import com.project.rentalCarPage.tables.JDBCClasses.Repositories.ReservationRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Reservation;
import com.project.rentalCarPage.tables.JDBCClasses.toolsToCustomizeNav;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;


    /**
     * Shows if a reservation operation was successful, then it shows a message.
     * Also, here a new reservation is inserted.
     * @param model
     * @param request
     * @param response
     * @return successfull_operation.html
     */
    @Transactional
    @PostMapping(value="/successfull_operation")
    public String showSuccessfullReservation(Model model, HttpServletRequest request, HttpServletResponse response){
        toolsToCustomizeNav.navCustomization(model,request,response);
        Cookie sessionCookie=null;
        Cookie dateCookie=null;
        Cookie carCookie=null;
        if(request.getCookies()!=null){
            for(Cookie c: request.getCookies()){
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_SESSION)){
                    sessionCookie=c;
                }
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_DATES)){
                    dateCookie=c;
                }
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_CARDATA)){
                    carCookie=c;
                }
            }
            if(sessionCookie!=null && dateCookie!=null && carCookie!=null){
                String[] sessionInfo=sessionCookie.getValue().split("#&#");
                ArrayList<String> dateInfo= (ArrayList<String>)Arrays.stream(dateCookie.getValue().split("TTT")).map(c->c.replace("T"," ")).collect(Collectors.toList());

                reservationRepository.insertReservation(Integer.valueOf(carCookie.getValue()),Integer.valueOf(sessionInfo[1]),1,dateInfo.get(2),dateInfo.get(0),dateInfo.get(1));
                ArrayList<Reservation> list=reservationRepository.checkLast();
                String message=String.format("<h3>Your reservation has the id: %s </h3> <br>",list.get(0).getIdreservation());
                message+=String.format("<h3>You must pickUp the car in %s</h3><br>",list.get(0).getPickupdate().toString().replace("T"," "));
                message+=String.format("<h3>You must return the car in %s</h3><br><br>",list.get(0).getReturndate().toString().replace("T"," "));
                message+="<h3><strong>Thanks for your reservation!</strong></h3>";
                model.addAttribute("reservationInfo",message);
            }
        }

        Cookie carData=new Cookie(toolsToCustomizeNav.COOKIE_CARDATA,"-1");
        Cookie dateData=new Cookie(toolsToCustomizeNav.COOKIE_DATES,"-1");


        carData.setMaxAge(1);
        response.addCookie(carData);
        response.addCookie(dateData);
        return "successfull_operation";
    }

    /**
     * Shows cancelReservation.html and cancel a reservation (changes reservation validity to 0)
     * @param model
     * @param request
     * @param response
     * @return cancelReservation.html
     */
    @Transactional
    @PostMapping(value="/cancelReservation")
    public String cancelReservation(Model model, HttpServletRequest request,HttpServletResponse response){
        toolsToCustomizeNav.navCustomization(model,request,response);
        Integer id=Integer.valueOf(request.getParameter("selectedRes"));
        reservationRepository.cancelReservation(id);
        String message=String.format("<h3>The reservation with id: %d has been cancelled</h3><br>",id);
        model.addAttribute("reservationInfo",message);
        return "cancelReservation";
    }

}
