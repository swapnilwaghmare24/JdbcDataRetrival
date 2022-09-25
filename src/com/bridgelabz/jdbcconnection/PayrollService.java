package com.bridgelabz.jdbcconnection;

import com.bridgelabz.sqlqueries.SqlQueries;

import java.sql.*;
import java.util.Scanner;

public class PayrollService {
        Connection con;
        SqlQueries sql;

        public void resetConnection() {
            con =  JdbcConnection.init().getConnection();
            sql = new SqlQueries();
        }

        public void getEmpData() {
            resetConnection();
            System.out.println(
                    "-------------------Employee Data-------------------------");
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql.SELECT_EMP_DATA);

                while (rs.next()) {
                    System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t"
                            + rs.getDate(3) + "\t" + rs.getString(4));
                }

                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateSalarySt(int emp_id, double basic_pay) {
            resetConnection();
            try {
                con = JdbcConnection.init().getConnection();
                Statement st = con.createStatement();
                double deduction = basic_pay * 0.10;
                double taxable_pay = basic_pay - deduction;
                double tax = taxable_pay * 0.20;
                double net_pay = taxable_pay - tax;
                int isUpdated = st
                        .executeUpdate("update payroll_tbl set basic_pay = "
                                + basic_pay + ", deduction = " + deduction
                                + ", taxable_pay = " + taxable_pay + "" + ", tax = "
                                + tax + ", net_pay = " + net_pay
                                + " where emp_id = " + emp_id);

                if (isUpdated == 1) {
                    System.out.println("Data is updated successfully");
                } else {
                    System.out.println("Something went wrong while updating the data.");
                }

                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        public void updateSalary(int emp_id, double basic_pay) {
            resetConnection();
            try {
                PreparedStatement ps = con.prepareStatement(sql.UPDATE_SALARY);
                ps.setDouble(1, basic_pay);
                double deduction = basic_pay * 0.10;
                ps.setDouble(2, deduction);
                double taxable_pay = basic_pay - deduction;
                ps.setDouble(3, taxable_pay);
                double tax = taxable_pay * 0.20;
                ps.setDouble(4, tax);
                double net_pay = taxable_pay - tax;
                ps.setDouble(5, net_pay);
                ps.setInt(6, emp_id);

                int isUpdated = ps.executeUpdate();

                if (isUpdated == 1) {
                    System.out.println("Data is updated successfully");
                    showPayrollData();
                } else {
                    System.out.println("Something went wrong while updating the data.");
                }

                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void showPayrollData() {
            resetConnection();
            System.out.println(
                    "-------------------Employee payroll data-----------------------");
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql.SELECT_EMP_PAYROLL_DATA);

                while (rs.next()) {
                    System.out.print(rs.getInt("emp_id") + "\t");
                    System.out.print(rs.getString("emp_name") + "\t");
                    System.out.print(rs.getInt("payroll_id") + "\t");
                    System.out.print(rs.getDouble("basic_pay") + "\t");
                    System.out.print(rs.getDouble("deduction") + "\t");
                    System.out.print(rs.getDouble("taxable_pay") + "\t");
                    System.out.print(rs.getDouble("tax") + "\t");
                    System.out.print(rs.getDouble("net_pay") + "\t");
                    System.out.println();
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        public void showEmpDeptPayrollData() {
            resetConnection();
            System.out.println(
                    "-------------------Employee payroll data-----------------------");
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql.SELECT_EMP_DEPT_PAYROLL_DATA);

                while (rs.next()) {
                    System.out.print(rs.getInt("emp_id") + "\t");
                    System.out.print(rs.getString("emp_name") + "\t");
                    System.out.print(rs.getDouble("basic_pay") + "\t");
                    System.out.print(rs.getString("dept_name") + "\t");
                    System.out.println();
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        public void getEmpDataByJoinDate(String startDate, String endDate) {
            resetConnection();
            System.out.println(
                    "---------------Employee data based on join date------------------");
            try {
                PreparedStatement ps = con
                        .prepareStatement(sql.SELECT_EMP_DATA_BY_JOIN_DATE);
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    System.out.print(rs.getInt("emp_id") + "\t");
                    System.out.print(rs.getString("emp_name") + "\t");
                    System.out.print(rs.getString("join_date") + "\t");
                    System.out.print(rs.getString("gender") + "\t");
                    System.out.println();
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void addEmpData() {
            Scanner sc = new Scanner(System.in);

            System.out.println("Enter the employee name : ");
            String empName = sc.nextLine();

            System.out.println("Enter the join date (yyyy-MM-dd) : ");
            String joinDate = sc.nextLine();

            System.out.println("Enter the gender (M/F/O) : ");
            String gender = sc.nextLine();

            System.out.println("Select the department from below options "
                    + "(if employee is working in multiple departments, mention multiple Ids separated by comma) : ");
            showDepartmentDetails();
            String dept_id = sc.nextLine();

            System.out.println("Enter the basic pay amount : ");
            double basic_pay = sc.nextDouble();

            addEmpData(empName, joinDate, gender, dept_id, basic_pay);
            sc.close();
        }

        private void addEmpData(String empName, String joinDate, String gender,
                                String dept_id, double basic_pay) {
            resetConnection();

            try {
                con.setAutoCommit(false);
                PreparedStatement ps = con.prepareStatement(sql.INSERT_EMP_DATA,
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, empName);
                ps.setString(2, joinDate);
                ps.setString(3, gender);
                int noOfRowAffected = ps.executeUpdate();

                if (noOfRowAffected == 1) {
                    System.out.println(empName + "'s Data inserted successfully.");
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        int generatedEmpId = rs.getInt(1);
                        String[] dept_ids = dept_id.split(",");
                        for (int i = 0; i < dept_ids.length; i++) {
                            int deptId = Integer.parseInt(dept_ids[i]);
                            addEmpDepartmentDetails(generatedEmpId, deptId);
                        }
                        addEmpPayrollDetails(generatedEmpId, basic_pay);
                    }
                } else {
                    con.rollback();
                    System.err.println(
                            "Something went wrong while inserting data to the DB.");
                }
                con.close();
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                System.err.println("Something went wrong while inserting data to the DB.");
                e.printStackTrace();
            }
        }

        private void addEmpPayrollDetails(int empId, double basic_pay) {
            try {
                PreparedStatement ps = con
                        .prepareStatement(sql.INSERT_EMP_PAYROLL_DATA);
                ps.setDouble(1, basic_pay);
                double deduction = basic_pay * 0.10;
                ps.setDouble(2, deduction);
                double taxable_pay = basic_pay - deduction;
                ps.setDouble(3, taxable_pay);
                double tax = taxable_pay * 0.20;
                ps.setDouble(4, tax);
                double net_pay = taxable_pay - tax;
                ps.setDouble(5, net_pay);
                ps.setInt(6, empId);
                int noOfRowsAffected = ps.executeUpdate();

                if (noOfRowsAffected == 1) {
                    con.commit();
                    System.out.println("Employee payroll data saved successfully.");
                } else {
                    con.rollback();
                    System.err.println(
                            "Something went wrong while inserting Employee payroll data in DB.");
                }
                con.close();
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                System.err.println("Something went wrong while inserting Employee payroll data in DB.");
                e.printStackTrace();
            }
        }

        private void addEmpDepartmentDetails(int empId, int deptId) {
            try {
                PreparedStatement ps = con
                        .prepareStatement(sql.INSERT_EMP_DEPT_DATA);
                ps.setInt(1, empId);
                ps.setInt(2, deptId);
                int noOfRowsAffected = ps.executeUpdate();

                if (noOfRowsAffected == 1) {
                    System.out.println("Employee department details added successfully");
                } else {
                    con.rollback();
                    System.err.println("Something went wrong while inserting employee department details.");
                }
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                System.err.println("Something went wrong while inserting employee department details.");
                e.printStackTrace();
            }

        }

        public void showDepartmentDetails() {
            resetConnection();

            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql.SELECT_DEPT_DATA);
                while (rs.next()) {
                    System.out.print(rs.getInt("dept_id") + ".\t");
                    System.out.print(rs.getString("dept_name"));
                    System.out.println();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

}
