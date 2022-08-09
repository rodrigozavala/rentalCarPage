package com.project.rentalCarPage.controllers;


import com.project.rentalCarPage.tables.JDBCClasses.*;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.*;
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

    @Autowired
    QueryJoinCarCarmodelRepository queryJoinCarCarmodelRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    CarRepository carRepository;

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
            showReservationsInfo(model,request,response);
            showCarsInfo(model,request,response);

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
            boolean carOp=(request.getParameter("carOp")!=null)?true:false;
            if(carOp){
                Integer makeAvailable=Integer.valueOf(request.getParameter("available"));
                Integer makeUnavailable=Integer.valueOf(request.getParameter("unavailable"));
                showReservationsInfo(model,request,response);
                if(makeAvailable!=-1){
                    carRepository.makeAvailable(makeAvailable);
                }
                if(makeUnavailable!=-1){
                    carRepository.makeUnavailable(makeUnavailable);
                }

            }

            boolean resOp=(request.getParameter("resOp")!=null)?true:false;
            if(resOp){
                showReservationsInfo(model,request,response);
                if(request.getParameter("selResToCancel")!=null){
                    Integer resCancel=Integer.valueOf(request.getParameter("selResToCancel"));
                    reservationRepository.cancelReservation(resCancel);
                }
                if(request.getParameter("selResToExecute")!=null){
                    Integer resExecute=Integer.valueOf(request.getParameter("selResToExecute"));

                    Reservation res=reservationRepository.findById(resExecute).get();
                    carRepository.setReservationAndClient(res.getIdreservation(),res.getIdclient(),res.getIdcar());
                    carRepository.makeUnavailable(res.getIdcar());
                }


            }

            if(request.getParameter("resIdSearch")!=null){
                Integer searchIdRes=Integer.valueOf(request.getParameter("resIdSearch"));
                if(searchIdRes!=-1){
                    showReservationsInfo(model,request,response,searchIdRes);
                }else{
                    showReservationsInfo(model,request,response);
                }
            }


            showCarsInfo(model,request,response);

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

    private void showReservationsInfo(Model model, HttpServletRequest request, HttpServletResponse response){
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
            result+=String.format("<img src=\"%s\" width=%d height=%d> <br>",q.getImagepath(),150,150);
            result+=String.format("Model name: %s <br></td>",q.getModelname());

            result+=String.format("<td>Has Automatic transmission?: %s<br>",(q.getAuttransmission()==1)?"Yes":"No");//1
            result+=String.format("Is the car available?: %s<br>",(q.getAvailability()==1)?"Yes":"No");
            result+=String.format("Km per L: %f<br>",q.getKmperl());
            result+=String.format("Number of seats: %d<br>",q.getPeoplecapacity());
            result+=String.format("Luggage capacity: %d<br></td>",q.getLuggagecapacity());


            result+=String.format("<td>Client name: %s %s <br>",q.getName(),q.getLastname());
            result+=String.format("Email: %s<br>",q.getEmail());
            result+=String.format("Phone number: %s <br></td>",q.getPhone());

            result+="<td><form method=\"get\" action=\"employeesMainPage\">";
            result+=String.format("<input type=\"hidden\" name=\"resOp\" value=%d>", 1);
            result+=String.format("<input type=\"hidden\" name=\"selResToCancel\" value=%d>", q.getIdreservation());
            result+="<button type=\"submit\"><strong>Cancel this <br> reservation</strong></button></form><br><br>\n";

            result+="<form method=\"get\" action=\"employeesMainPage\">";
            result+=String.format("<input type=\"hidden\" name=\"resOp\" value=%d>", 1);
            result+=String.format("<input type=\"hidden\" name=\"selResToExecute\" value=%d>", q.getIdreservation());
            result+=String.format("<input type=\"hidden\" name=\"carToReserve\" value=%d>", q.getIdcar());
            result+="<button type=\"submit\"><strong>Link car to <br> reservation</strong></button></form></td>\n";

            result+="</tr>";
        }
        result+="</table>";
        model.addAttribute("employeeTables1",result);

    }
    private void showCarsInfo(Model model, HttpServletRequest request, HttpServletResponse response){
        ArrayList<QueryJoinCarCarmodel> list0=queryJoinCarCarmodelRepository.findAll();
        String result="<table class=\"table table-striped table-hover\">";
        for(QueryJoinCarCarmodel q: list0){
            result+="<tr>";

            result+=String.format("<td>Id Car:%d <br>",q.getIdcar());
            result+=String.format("<img src=\"%s\" width=%d height=%d> <br>",q.getImagepath(),150,150);
            result+=String.format("Model name: %s <br></td>",q.getModelname());

            result+=String.format("<td>Has Automatic transmission?: %s<br>",(q.getAuttransmission()==1)?"Yes":"No");//1
            result+=String.format("Is the car available?: %s<br>",(q.getAvailability()==1)?"Yes":"No");
            result+=String.format("Km per L: %f<br>",q.getKmperl());
            result+=String.format("Price per day: $ %d <br>",q.getPriceperday());
            result+=String.format("Number of seats: %d<br>",q.getPeoplecapacity());
            result+=String.format("Luggage capacity: %d<br></td>",q.getLuggagecapacity());

            result+="<td><form method=\"get\" action=\"employeesMainPage\">";
            result+=String.format("<input type=\"hidden\" name=\"carOp\" value=%d>", 1);
            result+=String.format("<input type=\"hidden\" name=\"unavailable\" value=%d>",-1);
            result+=String.format("<input type=\"hidden\" name=\"available\" value=%d>", q.getIdcar());
            result+=String.format("<button %s type=\"submit\"><strong>Set car as <br> available</strong></button></form></td>\n",
                    (q.getAvailability()==1)?"disabled":"");

            result+="<td><form method=\"get\" action=\"employeesMainPage\">";
            result+=String.format("<input type=\"hidden\" name=\"carOp\" value=%d>", 1);
            result+=String.format("<input type=\"hidden\" name=\"unavailable\" value=%d>", q.getIdcar());
            result+=String.format("<input type=\"hidden\" name=\"available\" value=%d>", -1);
            result+=String.format("<button %s type=\"submit\"><strong>Set car as <br> unavailable </strong></button></form></td>\n",
                    (q.getAvailability()==1)?"":"disabled");

            result+="</tr>";
        }
        result+="</table>";

        model.addAttribute("employeeTables2",result);
    }

    private void showReservationsInfo(Model model, HttpServletRequest request, HttpServletResponse response,Integer idRes){
        ArrayList<QueryJoinReservation>list=(ArrayList<QueryJoinReservation>)query.findReservationsByResId(idRes);
        if(list.size()==0){
            String message="<h2>There is no such reservation</h2>";
            model.addAttribute("employeeTables1",message);
            return;
        }
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
            result+=String.format("<img src=\"%s\" width=%d height=%d> <br>",q.getImagepath(),150,150);
            result+=String.format("Model name: %s <br></td>",q.getModelname());

            result+=String.format("<td>Has Automatic transmission?: %s<br>",(q.getAuttransmission()==1)?"Yes":"No");//1
            result+=String.format("Is the car available?: %s<br>",(q.getAvailability()==1)?"Yes":"No");
            result+=String.format("Km per L: %f<br>",q.getKmperl());
            result+=String.format("Number of seats: %d<br>",q.getPeoplecapacity());
            result+=String.format("Luggage capacity: %d<br></td>",q.getLuggagecapacity());


            result+=String.format("<td>Client name: %s %s <br>",q.getName(),q.getLastname());
            result+=String.format("Email: %s<br>",q.getEmail());
            result+=String.format("Phone number: %s <br></td>",q.getPhone());

            result+="<td><form method=\"get\" action=\"employeesMainPage\">";
            result+=String.format("<input type=\"hidden\" name=\"resOp\" value=%d>", 1);
            result+=String.format("<input type=\"hidden\" name=\"selResToCancel\" value=%d>", q.getIdreservation());
            result+="<button type=\"submit\"><strong>Cancel this <br> reservation</strong></button></form><br><br>\n";

            result+="<form method=\"get\" action=\"employeesMainPage\">";
            result+=String.format("<input type=\"hidden\" name=\"resOp\" value=%d>", 1);
            result+=String.format("<input type=\"hidden\" name=\"selResToExecute\" value=%d>", q.getIdreservation());
            result+=String.format("<input type=\"hidden\" name=\"carToReserve\" value=%d>", q.getIdcar());
            result+="<button type=\"submit\"><strong>Link car to <br> reservation</strong></button></form></td>\n";

            result+="</tr>";
        }
        result+="</table>";
        model.addAttribute("employeeTables1",result);

    }
}
