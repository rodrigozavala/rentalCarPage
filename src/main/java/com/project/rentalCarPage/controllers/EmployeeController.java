package com.project.rentalCarPage.controllers;


import com.project.rentalCarPage.tables.JDBCClasses.Employee;
import com.project.rentalCarPage.tables.JDBCClasses.QueryJoinReservation;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.EmployeeRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.QueryJoinReservationRepository;
import com.project.rentalCarPage.tables.JDBCClasses.toolsToCustomizeNav;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Controller
@RequestMapping(path="/")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    QueryJoinReservationRepository query;

    @GetMapping(value = "/loginEmployees")
    public String loginEmployee(){
        return "loginEmployees";
    }
    @PostMapping(value="/employeesMainPage")
    public String mainPageEmployee(Model model, HttpServletRequest request, HttpServletResponse response){
        String user=request.getParameter("userName");
        String password=request.getParameter("password");
        ArrayList<Employee> list=new ArrayList<Employee>();
        list=employeeRepository.findByUserNameAndPassword(user,password);
        if(list.size()==0){
            String message="<h1>Your User Name or Password is incorrect,<br> please check again</h1>";
            model.addAttribute("",message);
        }else{//userName and password correct
            String cookieMessage=list.get(0).getUsername()+"###"+list.get(0).getIdemployee();
            Cookie sessionEmployee=new Cookie(toolsToCustomizeNav.COOKIE_EMPLOYEE,cookieMessage);
            response.addCookie(sessionEmployee);



        }
        return "employeesMainPage";
    }
    @GetMapping(value="/employeesMainPage")
    public String mainPageEmployeeGet(Model model, HttpServletRequest request, HttpServletResponse response){
        String cookieContent="";
        if(request.getCookies()!=null){
            for(Cookie c: request.getCookies()){
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_EMPLOYEE)){
                    cookieContent+=c.getValue();
                }
            }
        }
        if(cookieContent.equals("")==false){

        }else{
            String message="<h1>There is no employee</h1>";
            model.addAttribute("employeeTables1",message);
        }
        return "employeesMainPage";
    }

    @GetMapping(value="/logoutEmployees")
    public String logoutEmployee(HttpServletRequest request, HttpServletResponse response){
        if(request.getCookies()!=null){
            for(Cookie c: request.getCookies()){
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_EMPLOYEE)){
                    Cookie erase=c;
                    erase.setValue("NULL");
                    erase.setMaxAge(1);
                    response.addCookie(erase);
                }
            }
        }
        return "logoutEmployees";
    }

    private void showRentalCarInfo(Model model, HttpServletRequest request, HttpServletResponse response){
        ArrayList<QueryJoinReservation>list=(ArrayList<QueryJoinReservation>)query.findAll();
        String result="<table class=\"table table-striped table-hover\">";
        for(QueryJoinReservation q: list){
            result+=(q.getValidity()==1)?"<tr class=\"table-success\">":"<tr class=\"table-danger\">";
            result+=String.format("<td>Reservation's Id: %d <br>",q.getIdreservation());//1
            result+=String.format("Price per day: $ %d <br>",q.getPriceperday());
            result+=String.format("Total amount: $ %f <br>", Float.valueOf(ChronoUnit.DAYS.between(q.getPickupdate(),q.getReturndate())*q.getPriceperday()));
            result+=String.format("PickUp Date: %s <br>",q.getPickupdate().toString().replace("T"," "));
            result+=String.format("Return Date: %s <br>",q.getReturndate().toString().replace("T"," "));
            result+=String.format("Reservation Date: %s <br>",q.getReservationdate().toString().replace("T"," "));
            result+=String.format("Is valid?: %s <br> </td>",(q.getValidity()==1)?"Yes":"No");
            result+=String.format("<td>Id Car:%d <br>",q.getIdcar());
            result+=String.format("<img src=\"%s\" width=%d height=%d> <br>",q.getImagepath(),100,100);
            result+=String.format("Has Automatic transmission?: %s<br>",(q.getAuttransmission()==1)?"Yes":"No");
            result+=String.format("Is the car available?: %s<br>",(q.getAvailability()==1)?"Yes":"No");
            result+=String.format("<br>",q.getKmperl());
            result+=String.format("<br>",q.getModelname());
            result+=String.format("<br>",q.getPeoplecapacity());
            result+=String.format("<br>",q.getLuggagecapacity());



            result+="</tr>";
        }
        result+="</table>";
        model.addAttribute("",result);

    }
}
