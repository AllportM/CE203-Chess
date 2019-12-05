package com.company;

public class Development extends Employee {
    protected String project;

    public Development(String fname, String sname, int salary, String dob, String project){
        this.fName = fname;
        this.sName = sname;
        this.salary = salary;
        this.dob = dob;
        this.project = project;
    }

    public void information(){
        System.out.println(String.format("Role: Development\nProject: %s\nFirstname: %s\nSurname: %s\n" +
                "DOB: %s\nSalary: £%s\n", project, fName, sName, dob, sFormat.format(salary)));
    }
}
