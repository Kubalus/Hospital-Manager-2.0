package sample;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;


public class Controller {

    @FXML
    private JFXTreeTableView<DiseaseLog> patientTableView;

    @FXML
    private JFXTreeTableView<DiseaseLog> doctorLogsTableView;

    @FXML
    private JFXTreeTableView<Patient> patientsTableView;

    @FXML
    private Label patientNameLabel;

    @FXML
    private Label patientHeadLabel;

    @FXML
    private Label doctorHeadLabel;


    @FXML
    private TabPane doctorPane;

    @FXML
    private TabPane mainPane;

    @FXML
    private Tab doctorMainTab;

    @FXML
    private Tab doctorPatientTab;

    @FXML
    private Tab doctorNewPatientTab;

    @FXML
    private JFXButton newPatientButton;

    @FXML
    private JFXButton newPatientGoBackButton;

    @FXML
    private JFXButton doctorPatientGoBackButton;

    @FXML
    private JFXButton LogGoBackButton;

    @FXML
    private JFXTextField NPFieldPESEL;

    private String message;
    public ServerConnector serverConnector;
    private static User user;

    public static void setUser(User User)
    {
        user = User;
    }

    public static User getUser()
    {
        return user;
    }

    @FXML
    private JFXButton dropDatabaseButton;

    @FXML
    private JFXSpinner spinner;

    @FXML
    private Tab patientTab;

    @FXML
    private Tab signInTab;

    @FXML
    private Tab doctorTab;

    @FXML
    private Tab DirectorTab;

    @FXML
    private void newPatientStart(ActionEvent event){
        doctorPane.getSelectionModel().select(doctorNewPatientTab);
    }

    @FXML
    private void dropDatabase(ActionEvent event){
        spinner.setVisible(true);
    }

    @FXML
    private JFXTextField loginField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton signInButton;

    private void SignIn(String response)
    {
        if(response == "failed")
        {
            // TODO: BAD LOGIN OR PASSWORD HANDLING
        }
        else
        {

            switch(response)
            {
                case "patient":
                    patientStart();
                    break;
                case "doctor":
                    doctorStart();
                    break;
                case "director":
                    directorStart();
                    break;
                default:
                    System.out.println(response);
                    break;
            }

        }


    }

    private void directorStart() {

    }

    private void doctorStart() {
        doctorHeadLabel.setText(serverConnector.getDoctorName(user.getPESEL()));
        String response = serverConnector.getPatients(user.getPESEL());
        String[][] data = serverConnector.createTable(response);
        DoctorInitializer.setData(data);
        DoctorInitializer.generatePatientNames(patientTableView);
        mainPane.getSelectionModel().select(doctorTab);


    }

    private void patientStart() {
        patientHeadLabel.setText(serverConnector.getPatientName(user.getPESEL()));
        PatientInitializer.generateDiseaseLogs(patientTableView, serverConnector.createTable(serverConnector.getPatientDiseases(user.getPESEL())));
    //    PatientInitializer.generateDiseaseLogs(doctorLogsTableView, serverConnector.createTable(serverConnector.getPatientDiseases(user.getPESEL())));
        mainPane.getSelectionModel().select(patientTab);
    }

    @FXML
    private void signIn(ActionEvent event)
    {
        SignIn(serverConnector.signIn(loginField.getText(), passwordField.getText()));
    }

    @FXML
    public void initialize(){
        serverConnector = ServerConnector.getINSTANCE();
        serverConnector.connectToServer();

        //DoctorInitializer.generatePatientNames(patientsTableView);

        RequiredFieldValidator validator = new RequiredFieldValidator();
        NPFieldPESEL.getValidators().add(validator);
        validator.setMessage("Nie wpisano identyfikatora PESEL!");

        NPFieldPESEL.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue);
                {
                    NPFieldPESEL.validate();
                }
            }
        });


        patientsTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                    Node node = ((Node) event.getTarget()).getParent();
                    System.out.println(node);
                    JFXTreeTableRow row;
                    if (node instanceof JFXTreeTableRow) {
                        row = (JFXTreeTableRow) node;
                        String FullName = ((Patient) row.getTreeItem().getValue()).getFullName();
                        System.out.println(FullName);
                        patientNameLabel.setText(FullName);
                        patientNameLabel.setTextAlignment(TextAlignment.CENTER);
                    }
            }
        });

        patientsTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Node node = ((Node) event.getTarget()).getParent();
                    System.out.println(node);
                    JFXTreeTableRow row;
                    if (node instanceof JFXTreeTableRow) {
                        row = (JFXTreeTableRow) node;
                        String FullName = ((Patient) row.getTreeItem().getValue()).getFullName();
                        doctorPane.getSelectionModel().select(doctorPatientTab);
                    }
                }
            }
        });


        newPatientGoBackButton.setOnAction((event) -> {
            doctorPane.getSelectionModel().select(doctorMainTab);
        });

        doctorPatientGoBackButton.setOnAction((event) -> {
            doctorPane.getSelectionModel().select(doctorMainTab);
        });

        LogGoBackButton.setOnAction((event) -> {
            doctorPane.getSelectionModel().select(doctorMainTab);
        });

    }


}
