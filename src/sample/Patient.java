package sample;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


class Patient extends RecursiveTreeObject<Patient>
{
    StringProperty FullName;
    StringProperty PESEL;

    public Patient (String fullName, String pesel) {
        FullName = new SimpleStringProperty(fullName);
        PESEL = new SimpleStringProperty(pesel);
    }

    public String getPESEL() {
        return PESEL.get();
    }

    public String getFullName() {
        return FullName.get();
    }
}