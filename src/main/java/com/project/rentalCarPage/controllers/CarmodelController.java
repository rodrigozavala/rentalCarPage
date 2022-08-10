package com.project.rentalCarPage.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.rentalCarPage.tables.JDBCClasses.*;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.CarRepository;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class CarmodelController {
    @Autowired
    private CarmodelRepository carmodelRepository;

    @Autowired
    private QueryJoinCarCarmodelRepository query;

    @Autowired
    private CarRepository carRepository;

    /**
     * Shows information about the car reserved
     * @param model
     * @param response
     * @param request
     * @param selection
     * @return paying.html
     */
    @PostMapping(value="/paying")
    public String showSelected(Model model,HttpServletResponse response,HttpServletRequest request, @RequestParam(value = "selection",required = false) Integer selection) {

        //HERE WE SHOW THE SELECTED CAR
        if(request.getParameter("selection")!=null){//if we come from carsBetween
            response.addCookie(new Cookie(toolsToCustomizeNav.COOKIE_CARDATA,request.getParameter("selection")));
            String table=convertToTable(query.findByIdCar(selection),false,request);
            model.addAttribute("vehicleInformation",table);
        }else{//we don't come from carsBetween, maybe we come from clientInfo, there we should show a message
            //explaining there is still a reservation to do
            if(request.getCookies()!=null){//we take the cookies
                for(Cookie c:request.getCookies()){
                    if(c.getName().equals(toolsToCustomizeNav.COOKIE_CARDATA)){
                        String table=convertToTable(query.findByIdCar(Integer.valueOf(c.getValue())),false,request);
                        model.addAttribute("vehicleInformation",table);
                    }
                    break;
                }
            }else{//there is no parameter selection and neither a cookie, then we must have an error
                String message="<h2>You haven't chose a car yet</h2>";
                model.addAttribute("vehicleInformation",message);
            }

        }


        toolsToCustomizeNav.navCustomization(model,request,response);
        Cookie sessionCookie;
        if(request.getCookies()!=null){
            for(Cookie c:request.getCookies()){
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_SESSION)){
                    sessionCookie=c;
                    if(sessionCookie.getValue().equals("null")==false){//there is an user

                        showCreditCardForm(model);
                    }else{//the is no user, we must encourage to login
                        String login="<div class=\"firstDiv\">\n" +
                                "<h1>Please, login so we can book your reservation</h1>"+
                                "  <form method=\"get\" action=\"http://localhost:8989/login\">\n" +
                                "      <input type=\"hidden\" id=\"Nothing\" name=\"Nothing\" value=\"200\">\n" +
                                "      <button type=\"submit\"><strong>Go to Login</strong></button>\n" +
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

    /**
     * Shows cars selected by some parameters
     * @param model
     * @param request
     * @param response
     * @param pickUpDate when to pickup the car
     * @param returnDate when to return the car
     * @param MaxPrice maxPrice of cars
     * @param CarType type of car
     * @param order ASC or DESC
     * @return CarsBetween.html
     */
    @GetMapping(value="/CarsBetween")
    public String selectCars(Model model, HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(name="PickUpDate", required = false) String pickUpDate,
                             @RequestParam(name="ReturnDate",required = false) String returnDate,
                             @RequestParam(name="MaxPricePerDay",required = false) Integer MaxPrice,
                             @RequestParam(name="TypeCar",required = false) String CarType,
                             @RequestParam(name="order",required = false) String order){
        toolsToCustomizeNav.navCustomization(model,request,response);
        if(pickUpDate==null || returnDate==null || CarType==null){
            String message="<h4>Enter the desired date to pick and return the vehicle <br> as well " +
                    "as the maximum price you want to pay (optional) and the type of car you want (optional)</h4>";
            model.addAttribute("tableInformation",message);

        }else{

            response.addCookie(toolsToCustomizeNav.addDatesCookie(request));
            ArrayList<QueryJoinCarCarmodel> list=new ArrayList<QueryJoinCarCarmodel>();
            if(order.equals("ASC")){
                 list=query.findByTimeFrameTypeAndPriceAsc(returnDate.replace("T"," ")+":00",pickUpDate.replace("T"," ")+":00","%"+CarType+"%",MaxPrice);
            }else{
                 list=query.findByTimeFrameTypeAndPriceDesc(returnDate.replace("T"," ")+":00",pickUpDate.replace("T"," ")+":00","%"+CarType+"%",MaxPrice);
            }
            if(list.size()!=0){
                String table= convertToTable(list,true,request);
                model.addAttribute("tableInformation",table);
            }else{
                String message=String.format("<h4>There are %d cars with this characteristics, <br> please chose again</h4>",list.size());
                model.addAttribute("tableInformation",message);
            }

        }
        return "CarsBetween";
    }

    /**
     * Converts a list of elements into a html table. If isTable==true
     * then it shows info to CarsBetween.html, otherwise, it shows info
     * to paying.html
     * @param list
     * @param isTable
     * @param request
     * @return a table in html format
     */
    private String convertToTable(ArrayList<QueryJoinCarCarmodel> list,Boolean isTable,HttpServletRequest request){
        //Change later to implement CSS
        String result="";
        //String result=String.format("<h3> %s %s</h3>",request.getRequestURL(),request.getPathInfo());
        if(request.getRequestURL().toString().contains("CarsBetween") && request.getParameter("PickUpDate")!=null){
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime pDate=LocalDateTime.parse(request.getParameter("PickUpDate").replace("T"," "),formatter);//@RequestParam(name=, required = false) String pickUpDate, @RequestParam(name=,required = false) String returnDate, @RequestParam(name="MaxPricePerDay",required = false) Integer MaxPrice, @RequestParam(name=,required = false) String CarType, @RequestParam(name="order",required = false) String order)
            LocalDateTime rDate=LocalDateTime.parse(request.getParameter("ReturnDate").replace("T"," "),formatter);
            String carType=request.getParameter("TypeCar");
            result+=String.format("<h3>PickUp Date: %s <br> Return Date: %s <br>",pDate.toString().replace("T"," "),rDate.toString().replace("T"," "));
            result+=String.format("Car Type: %s </h3><br>",(carType.length()==0)?"Not specified":carType);

        }else if(request.getRequestURL().toString().contains("paying") && request.getCookies()!=null){
            System.out.println("##############################!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Cookie sessionCookie=null;
            Cookie carDataCookie=null;
            Cookie dateCookie=null;
            for(Cookie c: request.getCookies()){
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_SESSION)){
                    sessionCookie=c;
                }else if(c.getName().equals(toolsToCustomizeNav.COOKIE_CARDATA)){
                    carDataCookie=c;
                }else if(c.getName().equals(toolsToCustomizeNav.COOKIE_DATES)){
                    dateCookie=c;
                }
            }
            if(carDataCookie!=null && dateCookie!=null){
                DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                Car selectedCar=carRepository.findById(Integer.valueOf(carDataCookie.getValue())).get();
                //String[] dates=dateCookie.getValue().split("TTT");
                ArrayList<String> dateList=(ArrayList<String>)Arrays.stream(dateCookie.getValue().split("TTT")).
                        map(s->s.replace("T"," ")).
                        collect(Collectors.toList());
                LocalDateTime pDate=LocalDateTime.parse(dateList.get(0),formatter);
                LocalDateTime rDate=LocalDateTime.parse(dateList.get(1),formatter);
                long days=ChronoUnit.DAYS.between(pDate,rDate);
                String totalAmount= String.valueOf(selectedCar.getPriceperday()*days);
                result+=String.format("<h3>PickUp Date: %s <br> Return Date: %s <br>",pDate.toString().replace("T"," "),rDate.toString().replace("T"," "));
                result+=String.format("Total Amount:$ %s PricePerDay: $ %d </h3><br>",totalAmount,selectedCar.getPriceperday());

            }

        }

        result+="<table class=\"table table-striped table-hover\">";
        //DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //LocalDateTime today=LocalDateTime.now();
        //LocalDateTime returnDate;
        for(QueryJoinCarCarmodel q: list){
            result+="<tr>";
            result+=String.format("<td><img src=\"%s\" width=%d height=%d> <br>\n",q.getImagepath(),100,100);//1
            result+=String.format("Model name:<br>%s  %d</td>\n",q.getModelname(),q.getIdcar());
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


    /**
     * Show the credit card form
     * @param model
     */
    private void showCreditCardForm(Model model){
        String information="<h2>Please introduce your <br> credit car information:</h2>\n" +
                "                <form class=\"form-group\" method=\"post\" action=\"successfull_operation\">\n" +
                "                    <p>Chose a credit card type:</p>\n" +
                "                    \n" +
                "                    <input type=\"radio\" id=\"VISA\" name=\"CreditCard\" value=\"VISA\">\n" +
                "                    <label for=\"VISA\">VISA</label><br>\n" +
                "\n" +
                "                    <input type=\"radio\" id=\"MASTERCARD\" name=\"CreditCard\" value=\"MASTERCARD\">\n" +
                "                    <label for=\"MASTERCARD\">Mastercard</label><br>\n" +
                "\n" +
                "                    <input type=\"radio\" id=\"AMERICAN_EXPRESS\" name=\"CreditCard\" value=\"AMERICAN_EXPRESS\">\n" +
                "                    <label for=\"AMERICAN_EXPRESS\">American Express</label><br>\n" +
                "\n" +
                "                    <input type=\"radio\" id=\"DISCOVER\" name=\"CreditCard\" value=\"DISCOVER\">\n" +
                "                    <label for=\"DISCOVER\">Discover</label><br>\n" +
                "\n" +
                "                    <input type=\"radio\" id=\"OTHER\" name=\"CreditCard\" value=\"OTHER\">\n" +
                "                    <label for=\"OTHER\">Other:</label>\n" +
                "                    <input type=\"text\" id=\"OTHERValue\" name=\"CreditCardOtherValue\"><br><br>\n" +
                "                    \n" +
                "                    <label for=\"CARD_NUMBER\">Card Number</label>\n" +
                "                    <input type=\"number\" id=\"CARD_NUMBER\"> <br>\n" +
                "\n" +
                "                    <label for=\"Exp_Date\">Expiration date</label>\n" +
                "                    <input type=\"date\" id=\"Exp_Date\" name=\"Exp_Date\"> <br>\n" +
                "\n" +
                "                    <label for=\"SecurityCode\">Security Code:</label>\n" +
                "                    <input type=\"number\"  id=\"SecurityCode\" name=\"SecurityCode\"> <br>\n" +
                "                    <button type=\"button\" onclick=\"validateAndSend()\">Sumbit</button>\n" +
                "                    <script>\n" +
                "                        function fillDate(dateString){\n" +
                "                            let myDate=new Date();\n" +
                "                            let arrStrings=dateString.split(\"-\");\n" +
                "                            myDate.setFullYear(arrStrings[0],Number(arrStrings[1])-1,arrStrings[2]);\n" +
                "                            return myDate;\n" +
                "                        }\n" +
                "                    \n" +
                "                        function validateAndSend(){\n" +
                "                            let radVISA=document.getElementById(\"VISA\").checked;\n" +
                "                            let radMC=document.getElementById(\"MASTERCARD\").checked;\n" +
                "                            let radAE=document.getElementById(\"AMERICAN_EXPRESS\").checked;\n" +
                "                            let radD=document.getElementById(\"DISCOVER\").checked;\n" +
                "\n" +
                "                            let radO=document.getElementById(\"OTHER\").checked;\n" +
                "                            let textOV=document.getElementById(\"OTHERValue\").value;\n" +
                "\n" +
                "                            let numberCN=document.getElementById(\"CARD_NUMBER\").value;\n" +
                "                            let numberSC=document.getElementById(\"SecurityCode\").value;\n" +
                "                            let expDate=document.getElementById(\"Exp_Date\").value;\n" +
                "                            \n" +
                "                            \n" +
                "                            if(radVISA==false && radMC==false && radAE==false && radD==false && radO==false){\n" +
                "                                alert(\"There is no Card Type option please select one\");\n" +
                "                                console.log(\"No Card Type\"+expDate+\"Hola\");\n" +
                "                            }else if(radO==true && textOV.length==0){\n" +
                "                                alert(\"Option 'Other' lacks specification\");\n" +
                "                                console.log(\"No 'Other' card type\");\n" +
                "                            }else if(numberCN.length==0){\n" +
                "                                alert(\"There is no Card Number\");\n" +
                "                                console.log(\"No Card Number\");\n" +
                "                            }else if(numberSC.length==0){\n" +
                "                                alert(\"There is no Security Number\");\n" +
                "                                console.log(\"No Security Number\");\n" +
                "                            }else if(expDate.length==0){\n" +
                "                                alert(\"There is no Expiration Date\");\n" +
                "                                console.log(\"No Expiration Date\");\n" +
                "                            }else{\n" +
                "                                let today=new Date();\n" +
                "                                let expiration=fillDate(expDate);\n" +
                "                                if(today>=expiration){\n" +
                "                                    alert(\"Card has expired\");\n" +
                "                                    console.log(\"Too late\");\n" +
                "\n" +
                "                                }else{\n" +
                "                                    \n" +
                "                                    document.querySelector(\"div.container div.businessInfo form[method='post']\").submit();\n" +
                "                                }\n" +
                "                                \n" +
                "                                \n" +
                "                            }\n" +
                "                        }\n" +
                "                    </script>\n" +
                "                </form>";
        model.addAttribute("businessInformation",information);
    }



}
