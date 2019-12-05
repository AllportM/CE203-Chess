package com.company;

import java.text.DecimalFormat;

public abstract class Employee {
    protected String fName;
    protected String sName;
    protected int salary;
    protected String dob;
    DecimalFormat sFormat = new DecimalFormat("###,###,###");

    public abstract void information();
}