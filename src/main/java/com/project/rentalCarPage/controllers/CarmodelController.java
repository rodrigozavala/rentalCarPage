package com.project.rentalCarPage.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.rentalCarPage.tables.JDBCClasses.*;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.CarmodelRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.ClientRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.QueryJoinCarCarmodelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/pickACar")
public class CarmodelController {
    @Autowired
    private CarmodelRepository carmodelRepository;

    @Autowired
    private QueryJoinCarCarmodelRepository query;


    @PostMapping(value="/paying")
    public String showSelected(Model model,HttpServletResponse response,HttpServletRequest request, @RequestParam("selection") Integer selection) {
        String table=convertToTable(query.findByIdCar(selection),false);
        model.addAttribute("vehicleInformation",table);
        response.addCookie(new Cookie(toolsToCustomizeNav.COOKIE_CARDATA,request.getParameter("selection")));
        toolsToCustomizeNav.navCustomization(model,request,response);
        Cookie sessionCookie;
        if(request.getCookies()!=null){
            for(Cookie c:request.getCookies()){
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_SESSION)){
                    sessionCookie=c;
                    if(sessionCookie.getValue().equals("null")==false){//there is an user
                        String businessMessage="<div>" +
                                "<h1>Please introduce your <br> credit car information:</h1>"+
                                "<form>"+
                                "</form>"+
                                "</div>";
                        model.addAttribute("businessInformation",businessMessage);
                    }else{//the is no user, we must encourage to login
                        String login="<div class=\"firstDiv\">\n" +
                                "<h1>Please, login so we can book your reservation</h1>"+
                                "  <form method=\"post\" action=\"http://localhost:8989/clientInfo\">\n" +
                                "      <label for=\"Email\">Email:</label>\n" +
                                "      <input type=\"email\" id=\"Email\" name=\"Email\"> <br> <br>\n" +
                                "      <label for=\"Password\">Password:</label>\n" +
                                "      <input type=\"password\" id=\"Password\" name=\"Password\"> <br> <br>\n" +
                                "      <button type=\"button\" onclick=\"validateAndSend()\"><strong>Login</strong></button>\n" +
                                "      <h1 class=\"WarningMessage\" id=\"WarningMessage\" ></h1>\n" +
                                "      <script>\n" +
                                "          function validateAndSend(){\n" +
                                "            let email=document.getElementById(\"Email\").value;\n" +
                                "            let password=document.getElementById(\"Password\").value;\n" +
                                "            if(email.length==0 && password.length==0){\n" +
                                "                alert(\"There is no email and neither password\");\n" +
                                "                console.log(\"No Email no Password\");\n" +
                                "            }else if(email.length==0){\n" +
                                "                alert(\"There is no email\");\n" +
                                "                console.log(\"No email\");\n" +
                                "            }else if(password.length==0){\n" +
                                "                alert(\"There is no password\");\n" +
                                "                console.log(\"No Password\");\n" +
                                "            }else{\n" +
                                "                document.querySelector(\"div.firstDiv form[method='post']\").submit();\n" +
                                "            }\n" +
                                "          }\n" +
                                "      </script>\n" +
                                "  </form>\n" +
                                "</div>";
                        model.addAttribute("businessInformation",login);

                    }
                    break;
                }
            }

        }
        return "paying";
    }
    @GetMapping(value="/CarsBetween")
    public String selectCars(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(name="PickUpDate", required = false) String pickUpDate, @RequestParam(name="ReturnDate",required = false) String returnDate, @RequestParam(name="MaxPricePerDay",required = false) Integer MaxPrice, @RequestParam(name="TypeCar",required = false) String CarType){
        toolsToCustomizeNav.navCustomization(model,request,response);
        if(pickUpDate==null || returnDate==null || CarType==null){
            String message="<h1>Enter the desired date to pick and return the vehicle <br> as well " +
                    "as the maximum price you want to pay (optional) and the type of car you want (optional)</h1>";
            model.addAttribute("tableInformation",message);

        }else{

            response.addCookie(toolsToCustomizeNav.addDatesCookie(request));
            ArrayList<QueryJoinCarCarmodel> list=query.findByTimeFrameTypeAndPriceAsc(returnDate.replace("T"," ")+":00",pickUpDate.replace("T"," ")+":00","%"+CarType+"%",MaxPrice);
            if(list.size()!=0){
                String table= convertToTable(list,true);
                model.addAttribute("tableInformation",table);
            }else{
                String message=String.format("<h1>There are %d cars with this characteristics, <br> please chose again</h1>",list.size());
                model.addAttribute("tableInformation",message);
            }

        }
        return "CarsBetween";
    }

    private String convertToTable(ArrayList<QueryJoinCarCarmodel> list,Boolean isTable){
        //Change later to implement CSS
        String result="<table>";
        for(QueryJoinCarCarmodel q: list){
            result+="<tr>";
            result+=String.format("<td><img src=\"%s\" width=%d height=%d> <br>\n",q.getImagepath(),100,100);//1
            result+=String.format("Model name:<br>%s</td>\n",q.getModelname());
            result+=String.format("<td>Car Type:<br> %s <br>\n",q.getCartype().replace("types:"," ").replace(":"," "));//2
            result+=String.format("Car Price Per Day $:<br> %d</td>\n",q.getPriceperday());
            result+=String.format("<td>Seats:<br> %d <br>\n",q.getPeoplecapacity());//3
            result+=String.format("Luggage: <br> %d </td>\n",q.getLuggagecapacity());
            result+=String.format("<td>Km per L:<br> %f<br>\n",q.getKmperl());//4
            result+=String.format("Automatic transmission:<br> %s</td>\n",(q.getAuttransmission()==1)?"Yes":"No");
            //Need to change this to send the id to
            //result+=String.format("<td><form method=\"get\" action=\"paying/?selection=%d\">",q.getIdcar());
            if(isTable){
                result+="<td><form method=\"post\" action=\"paying\">";
                result+=String.format("<input type=\"hidden\" name=\"selection\" value=%d>", q.getIdcar());
                result+="<button type=\"submit\"><strong>Chose this <br> car </strong></button></form></td>\n";
            }
            result+="</tr>";
        }
        result+="</table>";
        return result;
    }



}
