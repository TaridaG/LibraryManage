package com.example.librarymanage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloController {

    @FXML
    private void openMembersView() throws IOException {
        openNewWindow("Members.fxml", "Members Management");
    }

    @FXML
    private void openBooksView() throws IOException {
        openNewWindow("Books.fxml", "Books Management");
    }

    private void openNewWindow(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
