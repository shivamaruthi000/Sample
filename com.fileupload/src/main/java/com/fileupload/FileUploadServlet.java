package com.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream inputStream = null;

        Part filePart = request.getPart("file");
        if (filePart != null) {
            inputStream = filePart.getInputStream();
        }

        Connection connection = null;
        String message = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileupload", "root", "12345");

            String sql = "INSERT INTO files (filename, filedata) values (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, filePart.getSubmittedFileName());

            if (inputStream != null) {
                statement.setBlob(2, inputStream);
            }

            int row = statement.executeUpdate();
            if (row > 0) {
                message = "File uploaded and saved into database";
            }
        } catch (ClassNotFoundException | SQLException e) {
            message = "ERROR: " + e.getMessage();
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        request.setAttribute("message", message);
        getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
    }
}
