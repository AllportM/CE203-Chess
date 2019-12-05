package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Employee[] list = new Employee[3];
        Manager newMan = new Manager("Frank", "Jones", 20000,
                "01/01/2000", "Production");
        Sales newSales = new Sales("Johnny", "English", 17000,
                "05/02/2001", 20, "Java");
        Development newDev = new Development("Eddy", "Smith", 30000,
                "02/07/1990", "Mint");
        list[0] = newDev;
        list[1] = newMan;
        list[2] = newSales;
        for (Employee e: list){
            e.information();
        }
    }
}
