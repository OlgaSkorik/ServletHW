package com.example.servlethw;

import com.example.servlethw.model.Employee;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "policeServlet", value = "/police")
public class PoliceServlet extends HttpServlet {
    public static final String url = "jdbc:mysql://localhost:3306/police";
    public static final String user = "root";
    public static final String password = "8893960";
    private Connection connection;
    private PreparedStatement preparedStatement;

    private List<Employee> employeeList = new ArrayList<>();

    public void init() {
        employeeList.add(new Employee(1, "Maksim", 45, "criminal investigation", 2500));
        employeeList.add(new Employee(2, "Anna", 38, "citizenship and migration", 2000));
        employeeList.add(new Employee(3, "Denis", 26, "state autoinspection", 1900));
        employeeList.add(new Employee(4, "Marta", 31, "execution of punishments", 2350));
        employeeList.add(new Employee(5, "Boris", 40, "protection of state secrets", 5000));
        employeeList.add(new Employee(6, "Vadim", 62, "criminal investigations", 12000));

        employeeList.forEach(myEmployee -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                preparedStatement = connection.prepareStatement(url);
                preparedStatement.execute("insert into employee " +
                        "values (" + myEmployee.getId() + ", '" + myEmployee.getName() + "', " + myEmployee.getAge() +
                        ", '" + myEmployee.getDepartment() + "', " + myEmployee.getSalary() + ");");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        try {
            ResultSet set = preparedStatement.executeQuery("select * from employee");
            while (set.next()) {
                String str = String.format("id: %d, name: %s, age: %d," +
                                " department: %s, salary: %d.\n",
                        set.getLong(1), set.getString(2),
                        set.getLong(3), set.getString(4),
                        set.getLong(5));
                writer.print(str);
                writer.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        writer.close();
    }

    public void destroy() {
        employeeList.clear();
        try {
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}