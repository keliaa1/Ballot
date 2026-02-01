package com.kelia.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.rmi.server.ServerCloneException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServlet extends HttpServlet {
    protected void service (HttpServletRequest req, HttpServletResponse res) throws ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try{
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/ballot",
                    "postgres",
                    "moonriver"
            );

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                HttpSession session = req.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", rs.getString("role"));
                session.setAttribute("voted", false);

                res.sendRedirect("index.jsp");

            } else{
                req.setAttribute("error", "invalid login");
                req.getRequestDispatcher("login.jsp").forward(req, res);

            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
