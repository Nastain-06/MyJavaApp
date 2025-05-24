package com.example.app;

// import com.example.app.db.DBUtil; // Tidak digunakan lagi jika UserService menangani koneksi
import com.example.app.model.User; // Ditambahkan
import com.example.app.service.UserService; // Ditambahkan
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException; // Masih diperlukan untuk menangkap exception dari service

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private final UserService userService; // Instance UserService

    // Variabel statis untuk menyimpan info user yang login (bisa diganti dengan solusi session yang lebih baik)
    public static User currentLoggedInUser = null;

    public LoginController() {
        this.userService = new UserService(); // Inisialisasi UserService
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Harap isi semua field.");
            return;
        }

        try {
            User user = userService.loginUser(username, password);

            if (user != null) {
                currentLoggedInUser = user; // Simpan info user yang login

                showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang, " + user.getUsername() + "! Peran Anda: " + user.getRole().name());

                // Buka halaman home.fxml
                // Pastikan path ke home.fxml benar.
                // Jika home.fxml ada di root 'resources', path-nya "/home.fxml".
                // Jika ada di subfolder 'view', path-nya "/view/home.fxml".
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml")); // Sesuaikan jika perlu
                Parent homeRoot = loader.load();

                // Dapatkan instance HomeController dan teruskan objek User
                HomeController homeController = loader.getController();
                if (homeController != null) { // Pastikan controller ditemukan
                    homeController.initUserData(currentLoggedInUser);
                } else {
                    System.err.println("HomeController tidak ditemukan oleh FXMLLoader!");
                }

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(homeRoot));
                stage.setTitle("Beranda - EDULIFE+ (" + user.getRole().name() + ")");
                stage.show();

            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau password salah.");
                currentLoggedInUser = null; // Reset jika gagal
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Database", "Koneksi gagal: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Aplikasi", "Terjadi kesalahan saat membuka halaman Home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            // Pastikan path ke register.fxml benar.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml")); // Sesuaikan jika perlu
            Parent registerRoot = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(registerRoot));
            stage.setTitle("Registrasi - EDULIFE+");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Aplikasi", "Gagal membuka halaman registrasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
