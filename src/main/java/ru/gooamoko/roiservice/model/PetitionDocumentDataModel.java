package ru.gooamoko.roiservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetitionDocumentDataModel {
    private PetitionDocumentModel data;

    public PetitionDocumentModel getData() {
        return data;
    }

    public void setData(PetitionDocumentModel data) {
        this.data = data;
    }
}
