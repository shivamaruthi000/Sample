package com.fileupload;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int fileId = 1; // Change this to the actual file ID you want to download

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileupload", "root", "12345");

            String sql = "SELECT filename, filedata FROM files WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, fileId);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String fileName = result.getString("filename");
                Blob blob = result.getBlob("filedata");

                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                InputStream inputStream = blob.getBinaryStream();
                OutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
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
    }
}
