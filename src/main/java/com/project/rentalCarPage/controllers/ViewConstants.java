package com.project.rentalCarPage.controllers;

public final class ViewConstants {

    private ViewConstants() {
        throw new RuntimeException("You are not supposed to be able to instantiated this class");
    }

    //CarController constants

    public static final String GET_CALL_PATH_BETWEEN = "/between";

    //CarModelController constants
    public static final String POST_CALL_PATH_PAYING = "/paying";
    public static final String HTML_PAYING = "paying";

    public static final String GET_CALL_PATH_CARS_BETWEEN = "/CarsBetween";
    public static final String HTML_CARS_BETWEEN = "CarsBetween";

    //ClientController constants

    public static final String POST_CALL_PATH_CLIENT_INFO = "/clientInfo";
    public static final String HTML_CLIENT_INFO = "clientInfo";

    public static final String GET_CALL_PATH_INDEX = "/index";
    public static final String HTML_INDEX = "index";

    public static final String GET_CALL_PATH_LOGIN = "/login";
    public static final String HTML_LOGIN = "login";

    public static final String GET_CALL_PATH_SIGN_UP = "/SignUp";
    public static final String HTML_SIGN_UP = "SignUp";

    public static final String GET_CALL_PATH_CHANGE_DATA = "/changeData";
    public static final String HTML_CHANGE_DATA = "changeData";

    public static final String GET_CALL_PATH_LOGOUT = "/logout";
    public static final String HTML_LOGOUT = "logout";

    //EmployeeController constants
    public static final String GET_CALL_PATH_LOGIN_EMPLOYEES = "/loginEmployees";
    public static final String HTML_LOGIN_EMPLOYEES = "loginEmployees";

    public static final String POST_CALL_PATH_EMPLOYEE_MAIN_PAGE = "/employeesMainPage";
    public static final String GET_CALL_PATH_EMPLOYEE_MAIN_PAGE = "/employeesMainPage";
    public static final String HTML_EMPLOYEE_MAIN_PAGE = "employeesMainPage";

    public static final String GET_CALL_PATH_LOGOUT_EMPLOYEES = "/logoutEmployees";
    public static final String HTML_LOGOUT_EMPLOYEES = "logoutEmployees";

    //ReservationController
    public static final String POST_CALL_PATH_SUCCESSFUL_OPERATION = "/successfull_operation";
    public static final String HTML_SUCCESSFUL_OPERATION = "successfull_operation";

    public static final String POST_CALL_PATH_CANCEL_RESERVATION = "/cancelReservation";
    public static final String HTML_CANCEL_RESERVATION = "cancelReservation";




}
