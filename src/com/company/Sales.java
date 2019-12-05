package com.company;

public class Sales extends Employee {
    protected int noSales;
    protected String special;

    public Sales (String fname, String sname, int salary, String dob, int noSales, String special){
        this.fName = fname;
        this.sName = sname;
        this.salary = salary;
        this.dob = dob;
        this.noSales = noSales;
        this.special = special;
    }

    public void information(){
        System.out.println(String.format("Role: Sales\nSpeciality: %s\nSales: %s\nFirstname: %s\nSurname: %s\n" +
                "DOB: %s\nSalary: £%s\n", special, noSales, fName, sName, dob, sFormat.format(salary)));
    }
}
