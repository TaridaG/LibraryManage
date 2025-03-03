package com.example.librarymanage;

import com.example.librarymanage.database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class BooksController {

    @FXML private TextField bookIdField;
    @FXML private TextField titleField;
    @FXML private TextField publisherField;
    @FXML private DatePicker publishDateField;
    @FXML private Button fetchBookButton;
    @FXML private Button saveBookButton;
    @FXML private Button updateBookButton;
    @FXML private Button deleteBookButton;
    @FXML private Button closeBookButton;

    @FXML
    public void initialize() {
        System.out.println("✅ BooksController initialized.");
    }

    // 🔍 Kitabı ID veya başlığa göre getir
    @FXML
    private void fetchBook() {
        String input = bookIdField.getText();
        String query = input.matches("\\d+") ? "SELECT * FROM books WHERE id = ?" : "SELECT * FROM books WHERE title ILIKE ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (input.matches("\\d+")) {
                pstmt.setInt(1, Integer.parseInt(input));
            } else {
                pstmt.setString(1, "%" + input + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                bookIdField.setText(String.valueOf(rs.getInt("id")));
                titleField.setText(rs.getString("title"));
                publisherField.setText(rs.getString("publisher"));
                Date date = rs.getDate("publish_date");
                if (date != null) {
                    publishDateField.setValue(date.toLocalDate());
                } else {
                    publishDateField.setValue(null);
                }

                System.out.println("✅ Book fetched: " + rs.getString("title"));
            } else {
                System.out.println("⚠ No book found with ID or Title: " + input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 💾 Yeni kitap ekle
    @FXML
    private void saveBook() {
        String title = titleField.getText();
        String publisher = publisherField.getText();
        LocalDate publishDate = publishDateField.getValue();
        String query = "INSERT INTO books (title, publisher, publish_date) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, title);
            pstmt.setString(2, publisher);
            pstmt.setDate(3, (publishDate != null) ? Date.valueOf(publishDate) : null);
            pstmt.executeUpdate();
            System.out.println("✅ Book saved: " + title);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📝 Kitap bilgilerini güncelle
    @FXML
    private void updateBook() {
        String bookId = bookIdField.getText();
        String title = titleField.getText();
        String publisher = publisherField.getText();
        LocalDate publishDate = publishDateField.getValue();
        String query = "UPDATE books SET title = ?, publisher = ?, publish_date = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, title);
            pstmt.setString(2, publisher);
            pstmt.setDate(3, (publishDate != null) ? Date.valueOf(publishDate) : null);
            pstmt.setInt(4, Integer.parseInt(bookId));
            pstmt.executeUpdate();
            System.out.println("✅ Book updated: " + title);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🗑 Kitabı sil
    @FXML
    private void deleteBook() {
        String bookId = bookIdField.getText();
        String query = "DELETE FROM books WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, Integer.parseInt(bookId));
            pstmt.executeUpdate();
            System.out.println("✅ Book deleted with ID: " + bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ❌ Pencereyi kapat
    @FXML
    private void closeWindow() {
        System.out.println("❌ Closing book window.");
        Stage stage = (Stage) closeBookButton.getScene().getWindow();
        stage.close();
    }
}
