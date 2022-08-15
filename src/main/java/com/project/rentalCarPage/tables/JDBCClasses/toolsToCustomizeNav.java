package com.project.rentalCarPage.tables.JDBCClasses;


import org.springframework.ui.Model;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class toolsToCustomizeNav {
    public static final String COOKIE_SESSION = "RCID";
    public static final String COOKIE_CARDATA = "RCDATA";
    public static final String COOKIE_DATES = "RCDATES";
    public static final String COOKIE_USER_NAME = "RCU";
    public static final String COOKIE_EMPLOYEE="AERCID";

    /**
     * Add nav customization to the model parameter
     * @param model
     * @param request
     * @param response
     */
    public static void navCustomization(Model model, HttpServletRequest request, HttpServletResponse response){
        String navInformation="<li>\n" +
                "                    <a href=\"http://localhost:8989/login\" >Login</a>\n" +
                "                </li>\n" +
                "                <li>\n" +
                "                    <a href=\"http://localhost:8989/SignUp\" >Sign Up</a>\n" +
                "                </li>";

        Cookie myCookieToken=null;
        Cookie userName=null;
        if (request.getCookies()!=null){//if we have cookies
            for(Cookie c: request.getCookies()){
                if(c.getName().equals(COOKIE_SESSION)){
                    myCookieToken=c;
                }
                if(c.getName().equals(COOKIE_USER_NAME)){
                    userName=c;
                }
            }
        }else{//first time in the website
            model.addAttribute("navInformation",navInformation);
            addIdSessionCookie(model,response);
        }
        if(myCookieToken!=null){//is not the first time the user has entered
            if(myCookieToken.getValue().equals("null")==false){
                //If the RCID cookie is a valid Token (not null), then userName must have a valid value as well,
                //then we proceed to put it inside the chain
                updateNavWithUserData(model,userName);

                }else{//The info token is equal to null, then userName is equal to null as well
                    model.addAttribute("navInformation",navInformation);
                }
        }else{//in this case we don't have a cookie inside (this is the first time the user has entered the website)
            model.addAttribute("navInformation",navInformation);
            addIdSessionCookie(model,response);
        }
    }

    /**
     * Add the ID cookie, the null tag avoids the necessity to track the user session in the server
     * by assigning the null tag to all the non-identified users
     * @param model
     * @param response
     */
    public static void addIdSessionCookie(Model model,HttpServletResponse response){
        Cookie cookie=new Cookie(COOKIE_SESSION,"null");
        response.addCookie(cookie);
    }

    /**
     * Add the user information to the model parameter to customize the nav element on html
     * @param model
     * @param userName
     */
    public static  void updateNavWithUserData(Model model,Cookie userName){
        String userInfo0=String.format("<li ><div class=\"dropdown\">\n" +
                "  <button class=\"btn btn-default dropdown-toggle\" type=\"button\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
                "    %s\n" +
                "  </button>\n" +
                "  <ul class=\"dropdown-menu\">\n" +
                "    <li>" +
                "    <form class=\"dropdown-item\" method=\"post\" action=\"/clientInfo\">\n" +
                "    <input type=\"hidden\" name=\"name\" value=\"value\" /> \n" +
                "    <a onclick=\"this.parentNode.submit();\">Check User Info</a>\n" +
                "   </form> </li>\n" +
                "    <li><a class=\"dropdown-item\" href=\"/logout\">logout</a></li>\n" +
                "  </ul>\n" +
                "</div></li><br><br>",userName.getValue());

        model.addAttribute("navInformation",userInfo0);
    }

    /**
     * Add CarData cookies to store information about the selected car
     * @param response
     * @param request
     */
    public static void addCarDataCookie(HttpServletResponse response,HttpServletRequest request){
        response.addCookie(new Cookie(COOKIE_CARDATA,request.getParameter("selection")));
    }


    /**
     * Convert String dates into a cookie value.
     * @param pickUpDate
     * @param returnDate
     * @return
     */
    public static String valuesToStringCookie(String pickUpDate,String returnDate){
        LocalDateTime today=LocalDateTime.now();
        String reservationdate=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(today).replace(" ","T");
        String result=pickUpDate+":00"+"TTT"+returnDate+":00"+"TTT"+reservationdate;
        return result;
    }

    /**
     * Add cookies corresponding to a timeframe
     * @param request
     * @return
     */
    public static Cookie addDatesCookie(HttpServletRequest request){
        String pickUpDate =request.getParameter("PickUpDate");
        String returnDate =request.getParameter("ReturnDate");
        return new Cookie(COOKIE_DATES,valuesToStringCookie(pickUpDate,returnDate));

    }


}
