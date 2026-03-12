package com.example.myapplication;

import java.io.Serializable;

public class Biography implements Serializable {
    private String id;
    private String name;
    private String birthPlace;
    private String historicalEvent;
    private String lifeData;
    private int imageResId;
    private String imageUri; // Para imágenes de la galería

    public Biography(String id, String name, String birthPlace, String historicalEvent, String lifeData, int imageResId) {
        this.id = id;
        this.name = name;
        this.birthPlace = birthPlace;
        this.historicalEvent = historicalEvent;
        this.lifeData = lifeData;
        this.imageResId = imageResId;
        this.imageUri = null;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBirthPlace() { return birthPlace; }
    public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }
    public String getHistoricalEvent() { return historicalEvent; }
    public void setHistoricalEvent(String historicalEvent) { this.historicalEvent = historicalEvent; }
    public String getLifeData() { return lifeData; }
    public void setLifeData(String lifeData) { this.lifeData = lifeData; }
    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}
