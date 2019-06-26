package sample;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public class DoctorInitializer {
    private static String[][] data;

    public static void setData(String[][] dataSet){
        data = dataSet;
    }
    public static void generatePatientNames(JFXTreeTableView patientTableView)
    {
        JFXTreeTableColumn<Patient,String> patient = new JFXTreeTableColumn<>("Pacjent");
        patient.setPrefWidth(330);
        patient.setMaxWidth(330);
        patient.setMinWidth(330);
        patient.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Patient, String> param) {
                return param.getValue().getValue().FullName;
            }
        });
        ObservableList<Patient> logs = FXCollections.observableArrayList();
        int length = data.length;
        for(int i = 0; i < length; i++){
            logs.add(new Patient(data[i][0],data[i][2]));
        }
        TreeItem<Patient> root = new RecursiveTreeItem<Patient>(logs, RecursiveTreeObject::getChildren);
        patientTableView.setRoot(root);
        patientTableView.setShowRoot(false);
        patientTableView.getColumns().setAll(patient);
    }
}
