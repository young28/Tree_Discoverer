package com.example.pinatala.tree_discoverer.model;

/**
 * Created by YouYang and Matteo Pontiggia on 14/12/16.
 */

public class TreeMarker {
    //Create the fields
    private int id;
    private String treeImageName;
    private String leafImageName;
    private String createDate;
    private Double latitude;
    private Double longitude;
    private String treeType;

    public TreeMarker(int id, String treeImageName, String leafImageName, String createDate,
                      Double lat, Double lon , String treeType){
        this.id = id;
        this.treeImageName = treeImageName;
        this.leafImageName = leafImageName;
        this.createDate = createDate;
        this.latitude = lat;
        this.longitude = lon;
        this.treeType = treeType;
    }

    public String getContent(){
        return id + ";" + treeImageName + ";" + leafImageName + ";" + createDate + ";" + latitude
                + ";" + longitude + ";"+ treeType;
    }


    //Set getter and setter of all the fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTreeImageName() {
        return treeImageName;
    }

    public void setTreeImageName(String treeImageName) {
        this.treeImageName = treeImageName;
    }

    public String getLeafImageName() {
        return leafImageName;
    }

    public void setLeafImageName(String leafImageName) {
        this.leafImageName = leafImageName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTreeType() {
        return treeType;
    }

    public void setTreeType(String treeType) {
        this.treeType = treeType;
    }
}
