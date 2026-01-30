package com.kelia.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class addCandidateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // 1️⃣ Load PostgreSQL Driver
            Class.forName("org.postgresql.Driver");

            // 2️⃣ Create connection
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/ballot",
                    "postgres",
                    "moonriver"
            );

            // 3️⃣ SQL query
            String sql = "INSERT INTO candidates (name) VALUES (?)";

            // 4️⃣ Prepare statement
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);

            // 5️⃣ Execute
            ps.executeUpdate();

            out.println("<h3>Candidate added successfully!</h3>");

            // 6️⃣ Close resources
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
