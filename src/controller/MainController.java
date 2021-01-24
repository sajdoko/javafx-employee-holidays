package controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Datasource;
import model.EmployeeSessionController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public Label employeeFullName;
    public Button buttonLogOut;
    public Label employeeDaysLeft;
    public Label holidaysResponse;
    public TextField fieldNrDays;

    public void btnLogOutOnClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure that you want to log out?");
        alert.setTitle("Log Out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            EmployeeSessionController.cleanEmployeeSession();
            Stage dialogStage;
            new Stage();
            Node node = (Node) actionEvent.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/login.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
        }
    }

    public void btnDecreaseHolidaysOnAction() {
        try {
            if (fieldNrDays.getText().isEmpty() || Integer.parseInt(fieldNrDays.getText()) == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Please enter an number!");
                alert.setTitle("Warning");
                alert.showAndWait();
            } else {
                int nrDays = Integer.parseInt(fieldNrDays.getText());
                if (nrDays > EmployeeSessionController.getEmployeeHolidays()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("You don't have " + nrDays + " days to take as vacation days!");
                    alert.setTitle("Warning");
                    alert.showAndWait();
                    fieldNrDays.setText(String.valueOf(0));

                } else {

                    Task<Boolean> decreaseHolidays = new Task<Boolean>() {
                        @Override
                        protected Boolean call() {
                            return Datasource.getInstance().decreaseHolidays(nrDays, EmployeeSessionController.getEmployeeUsername());
                        }
                    };

                    decreaseHolidays.setOnSucceeded(e -> {
                        if (decreaseHolidays.valueProperty().get()) {
                            String daysLeft = String.valueOf(EmployeeSessionController.decreaseEmployeeHolidaysSession(nrDays));
                            employeeDaysLeft.setText(daysLeft);
                            fieldNrDays.setText(String.valueOf(0));
                            holidaysResponse.setText("Holidays saved successfully!");
                        } else {
                            holidaysResponse.setText("Holidays weren't saved!");
                        }
                        holidaysResponse.setVisible(true);
                    });

                    new Thread(decreaseHolidays).start();
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.setTitle("Error");
            alert.showAndWait();
            fieldNrDays.setText(String.valueOf(0));
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeeFullName.setText(EmployeeSessionController.getEmployeeFullName());
        employeeDaysLeft.setText(String.valueOf(EmployeeSessionController.getEmployeeHolidays()));
        employeeDaysLeft.setVisible(true);
    }
}