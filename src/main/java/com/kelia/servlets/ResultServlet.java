package com.kelia.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.lang.Class.forName;

public class ResultServlet extends HttpServlet {
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<h2>Voting results</h2>");

        try{
            Class.forName("org.postgresql.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/ballot",
                    "postgres",
                    "moonriver"
            );
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT id, name, votes FROM candidates"
            );
            out.println("<table border='1'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Votes</th></tr>");

            while (rs.next()){
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getInt("votes") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " +e.getMessage() + "</h3>");
        }

        out.println("<br><a href='index.jsp'>Back</a>");
    }
}
