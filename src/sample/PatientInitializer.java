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

public class PatientInitializer {


    public static void generateDiseaseLogs(JFXTreeTableView patientTableView, String[][] resultArray)
    {
        JFXTreeTableColumn<DiseaseLog, String> Disease = new JFXTreeTableColumn<>("Dolegliwość");
        Disease.setPrefWidth(147);
        Disease.setMaxWidth(147);
        Disease.setMinWidth(147);
        Disease.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DiseaseLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DiseaseLog, String> param) {
                return param.getValue().getValue().DiseaseName;
            }
        });

        JFXTreeTableColumn<DiseaseLog, String> Log = new JFXTreeTableColumn<>("Opis");
        Log.setPrefWidth(400);
        Log.setMaxWidth(400);
        Log.setMinWidth(400);
        Log.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DiseaseLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DiseaseLog, String> param) {
                return param.getValue().getValue().LogDescription;
            }
        });

        JFXTreeTableColumn<DiseaseLog, String> LogDate = new JFXTreeTableColumn<>("Data");
        LogDate.setPrefWidth(150);
        LogDate.setMaxWidth(150);
        LogDate.setMinWidth(150);
        LogDate.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DiseaseLog, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DiseaseLog, String> param) {
                return param.getValue().getValue().Date;
            }
        });

        ObservableList<DiseaseLog> logs = FXCollections.observableArrayList();
        int length = resultArray.length;
        for(int i=0; i<length; i++)
        {
            logs.add(new DiseaseLog(resultArray[i][0], resultArray[i][1], resultArray[i][2]));
        }


        TreeItem<DiseaseLog> root = new RecursiveTreeItem<DiseaseLog>(logs, RecursiveTreeObject::getChildren);
        patientTableView.setRoot(root);
        patientTableView.setShowRoot(false);
        patientTableView.getColumns().setAll(Disease, Log, LogDate);
    }
}
