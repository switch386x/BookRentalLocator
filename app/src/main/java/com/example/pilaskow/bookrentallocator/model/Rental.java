package com.example.pilaskow.bookrentallocator.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Rental {
    String iSBNNumber;

    public String getiSBNNumber() {
        return iSBNNumber;
    }

    public String getDateOfRental() {
        return dateOfRental;
    }

    public String getDateOfReturn() {
        return dateOfReturn;
    }

    String dateOfRental;
    String dateOfReturn = "";

    public Rental(String iSBNNumber){
        this.iSBNNumber = iSBNNumber;
        dateOfRental = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

    public Rental(){}

}
