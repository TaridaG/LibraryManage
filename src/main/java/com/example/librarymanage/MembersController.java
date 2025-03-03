package com.example.librarymanage;

import com.example.librarymanage.database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MembersController {

    @FXML private TextField memberIdField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField telephoneField;
    @FXML private Button fetchMemberButton;
    @FXML private Button saveMemberButton;
    @FXML private Button updateMemberButton;
    @FXML private Button deleteMemberButton;
    @FXML private Button closeMemberButton;

    @FXML
    public void initialize() {
        System.out.println("✅ MembersController initialized.");
    }

    // 🔍 Kullanıcıyı ID’ye veya ismine göre getir
    @FXML
    private void fetchMember() {
        String input = memberIdField.getText();
        String query;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     input.matches("\\d+") ? "SELECT * FROM members WHERE id = ?" :
                             "SELECT * FROM members WHERE name ILIKE ?")) {

            if (input.matches("\\d+")) { // Eğer sadece rakam içeriyorsa ID olarak arama yap
                pstmt.setInt(1, Integer.parseInt(input));
            } else { // Eğer metinse, isim ile arama yap
                pstmt.setString(1, "%" + input + "%"); // İsmin geçtiği yerleri bul
            }

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                addressField.setText(rs.getString("address"));
                telephoneField.setText(rs.getString("telephone"));
                memberIdField.setText(String.valueOf(rs.getInt("id"))); // ID'yi de gösterebiliriz
                System.out.println("✅ Member fetched: " + rs.getString("name"));
            } else {
                System.out.println("⚠ No member found with ID or Name: " + input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 💾 Yeni üye ekle
    @FXML
    private void saveMember() {
        String name = nameField.getText();
        String address = addressField.getText();
        String telephone = telephoneField.getText();
        String query = "INSERT INTO members (name, address, telephone) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, telephone);
            pstmt.executeUpdate();
            System.out.println("✅ Member saved: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📝 Üye bilgilerini güncelle
    @FXML
    private void updateMember() {
        String memberId = memberIdField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String telephone = telephoneField.getText();
        String query = "UPDATE members SET name = ?, address = ?, telephone = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, telephone);
            pstmt.setInt(4, Integer.parseInt(memberId));
            pstmt.executeUpdate();
            System.out.println("✅ Member updated: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🗑 Üyeyi sil
    @FXML
    private void deleteMember() {
        String memberId = memberIdField.getText();
        String query = "DELETE FROM members WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, Integer.parseInt(memberId));
            pstmt.executeUpdate();
            System.out.println("✅ Member deleted with ID: " + memberId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ❌ Pencereyi kapat
    @FXML
    private void closeWindow() {
        System.out.println("❌ Closing member window.");
        Stage stage = (Stage) closeMemberButton.getScene().getWindow();
        stage.close();
    }
}
