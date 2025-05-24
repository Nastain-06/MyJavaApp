package com.example.app;

import com.example.app.model.Role; // Ditambahkan
import com.example.app.model.User; // Ditambahkan
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label; // Ditambahkan untuk menampilkan info user
import javafx.stage.Stage;

import java.io.IOException; // Ditambahkan untuk FXMLLoader

public class HomeController {

    @FXML
    private Label welcomeLabel; // Tambahkan <Label fx:id="welcomeLabel" /> di home.fxml Anda

    private User currentUser;

    /**
     * Menginisialisasi data pengguna setelah login berhasil.
     * Metode ini dipanggil oleh LoginController.
     * @param user Objek User yang berisi data pengguna yang login.
     */
    public void initUserData(User user) {
        this.currentUser = user;
        if (this.currentUser != null) {
            System.out.println("HomeController: Pengguna '" + currentUser.getUsername() + "' login dengan peran: " + currentUser.getRole());
            if (welcomeLabel != null) {
                welcomeLabel.setText("Selamat datang, " + currentUser.getUsername() + "! (Peran: " + currentUser.getRole().name() + ")");
            }

            // Contoh logika berdasarkan peran
            if (currentUser.getRole() == Role.ADMIN) {
                // Tampilkan atau aktifkan fitur khusus admin
                System.out.println("Admin terdeteksi. Fitur admin dapat diaktifkan di sini.");
                // Misalnya: adminSpecificButton.setVisible(true);
            } else {
                // Sembunyikan atau nonaktifkan fitur khusus admin
                System.out.println("User biasa terdeteksi.");
                // Misalnya: adminSpecificButton.setVisible(false);
            }
        } else {
            System.err.println("HomeController: initUserData dipanggil dengan objek User null.");
            if (welcomeLabel != null) {
                welcomeLabel.setText("Selamat datang!");
            }
        }
    }

    @FXML
    private void handleEcommerce(ActionEvent event) {
        try {
            // Pastikan path ke ecommerce.fxml benar
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ecommerce.fxml"));
            Parent root = loader.load();

            // Jika EcommerceController juga perlu data pengguna:
            // EcommerceController ecommerceController = loader.getController();
            // if (ecommerceController != null && currentUser != null) {
            // ecommerceController.initUserData(currentUser);
            // }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("E-Commerce - EDULIFE+");
            stage.show(); // Ditambahkan untuk menampilkan stage
        } catch (IOException e) { // Lebih spesifik menangkap IOException
            showError("Gagal membuka halaman E-Commerce.", e);
        } catch (Exception e) { // Menangkap exception umum lainnya
            showError("Terjadi kesalahan tak terduga.", e);
        }
    }

    @FXML
    private void handleDailyActivity(ActionEvent event) {
        try {
            // Pastikan path ke dailyactivity.fxml benar
            // Jika ada di subfolder 'view', path-nya "/view/dailyactivity.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dailyactivity.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Daily Activity - EDULIFE+");
            stage.show(); // Ditambahkan untuk menampilkan stage
        } catch (IOException e) { // Lebih spesifik menangkap IOException
            showError("Gagal membuka halaman Daily Activity.", e);
        } catch (Exception e) { // Menangkap exception umum lainnya
            showError("Terjadi kesalahan tak terduga.", e);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Reset user yang login di LoginController
            LoginController.currentLoggedInUser = null;
            this.currentUser = null; // Reset juga user di HomeController

            // Pastikan path ke login.fxml benar
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - EDULIFE+");
            stage.show(); // Ditambahkan untuk menampilkan stage
        } catch (IOException e) { // Lebih spesifik menangkap IOException
            showError("Gagal logout dan kembali ke halaman login.", e);
        } catch (Exception e) { // Menangkap exception umum lainnya
            showError("Terjadi kesalahan tak terduga saat logout.", e);
        }
    }

    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText(null);
        // Memberikan detail error yang lebih baik di alert
        String content = message;
        if (e != null) {
            content += "\nDetail: " + e.getMessage();
            e.printStackTrace(); // Tetap print stack trace untuk debugging di konsol
        }
        alert.setContentText(content);
        alert.showAndWait();
    }
}
