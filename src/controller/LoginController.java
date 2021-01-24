package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Employee;
import model.EmployeeSessionController;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    public TextField usernameField;

    Stage dialogStage = new Stage();
    Scene scene;

    public void handleLoginButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        String providedUsername = usernameField.getText();

        if (providedUsername == null || providedUsername.isEmpty()) {
            alertBox("Please enter the Username", null, "Login Failed!");
        } else {

            Employee employee = model.Datasource.getInstance().checkEmployee(providedUsername);

            if (employee.getUsername() == null || employee.getUsername().isEmpty()) {
                alertBox("There is no Employee registered with that username!", null, "Login Failed!");
            } else {

                new EmployeeSessionController(
                        (int) employee.getId(),
                        employee.getFullName(),
                        employee.getUsername(),
                        employee.getHolidays()
                );

                Node node = (Node) actionEvent.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                scene = new Scene(FXMLLoader.load(getClass().getResource("../view/app.fxml")), 390, 600);
                dialogStage.setScene(scene);
                dialogStage.show();
            }
        }
    }

    public static void alertBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
