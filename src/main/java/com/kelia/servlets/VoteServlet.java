package com.kelia.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class VoteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        // 1️⃣ Check session FIRST
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        Boolean voted = (Boolean) session.getAttribute("voted");
        if (voted != null && voted) {
            out.println("<h3>You have already voted!</h3>");
            return;
        }

        // 2️⃣ Read input
        int candidateId = Integer.parseInt(req.getParameter("candidateId"));

        try {
            // 3️⃣ DB connection
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/ballot",
                    "postgres",
                    "moonriver"
            );

            String sql = "UPDATE candidates SET votes = votes + 1 WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, candidateId);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                session.setAttribute("voted", true);
                out.println("<h3>Vote recorded successfully!</h3>");
            } else {
                out.println("<h3>Invalid candidate!</h3>");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }

        out.println("<br><a href='index.jsp'>Back</a>");
    }
}
