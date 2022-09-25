package com.bridgelabz.JdbcRunner;

import com.bridgelabz.jdbcconnection.PayrollService;

public class JdbcRunner {

    public static void main(String[] args) {

        PayrollService service = new PayrollService();

        service.getEmpData();

        System.out.println("------------------------------------------");

        service.addEmpData();
        System.out.println("------------------------------------------");
        service.showEmpDeptPayrollData();
    }
}
