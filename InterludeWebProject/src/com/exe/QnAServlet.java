package com.exe;

import java.io.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@jakarta.servlet.annotation.WebServlet("/QnAServlet")
public class QnAServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/QnADatabase";
    private static final String DB_USER = "qna_user";
    private static final String DB_PASSWORD = "securepassword";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String question = request.getParameter("question");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO QnA (question) VALUES (?)")) {
            stmt.setString(1, question);
            stmt.executeUpdate();
            response.getWriter().write("Question submitted successfully.");
        } catch (SQLException e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, question, answer, created_at, answered_at FROM QnA")) {
            StringBuilder json = new StringBuilder("[");
            while (rs.next()) {
                if (json.length() > 1) json.append(",");
                json.append(String.format(
                    "{\"id\":%d,\"question\":\"%s\",\"answer\":\"%s\",\"created_at\":\"%s\",\"answered_at\":\"%s\"}",
                    rs.getInt("id"),
                    rs.getString("question").replace("\"", "\\\""),
                    rs.getString("answer") == null ? "" : rs.getString("answer").replace("\"", "\\\""),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("answered_at") == null ? "" : rs.getTimestamp("answered_at")
                ));
            }
            json.append("]");
            response.getWriter().write(json.toString());
        } catch (SQLException e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
