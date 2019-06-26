package sample;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


class DiseaseLog extends RecursiveTreeObject<DiseaseLog>
{
    StringProperty DiseaseName;
    StringProperty LogDescription;
    StringProperty Date;

    public DiseaseLog(String diseaseName, String logDescription, String date) {
        DiseaseName = new SimpleStringProperty(diseaseName);
        LogDescription = new SimpleStringProperty(logDescription);
        Date = new SimpleStringProperty(date);
    }
}