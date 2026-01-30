package com.kelia.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class VoteServlet extends HttpServlet {
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int candidateId = Integer.parseInt(req.getParameter("candidateId"));
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try{
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/ballot",
                    "postgres",
                    "moonriver"
            );
            String sql = "UPDATE candidates SET VOTES = votes + 1 WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, candidateId);

            int updated = ps.executeUpdate();
            if (updated > 0){
                out.println("<h3>Vote recorded successfully</h3>");

            }else{
                out.println("<h3>Invalid candidate id</h3>");
            }
            ps.close();
            con.close();
        } catch (Exception e){
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage()+ "</h3>");
        }
        out.println("<br><a href='index.jsp'>Back</a>");
    }
}
