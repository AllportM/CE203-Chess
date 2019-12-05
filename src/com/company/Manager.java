package com.company;

public class Manager extends Employee {
    protected String dep;

    public Manager (String fname, String sname, int salary, String dob, String dep) {
        this.fName = fname;
        this.sName = sname;
        this.salary = salary;
        this.dob = dob;
        this.dep = dep;
    }

    public void information(){
        System.out.println(String.format("Role: Manager\nDepartment: %s\nFirstname: %s \nSurname: %s\nDOB: %s\n" +
                "Salary: £%s\n", dep, fName, sName, dob, sFormat.format(salary)));
    }
}
