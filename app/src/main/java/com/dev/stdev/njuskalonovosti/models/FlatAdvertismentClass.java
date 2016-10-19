package com.dev.stdev.njuskalonovosti.models;

import java.io.Serializable;


public class FlatAdvertismentClass implements Serializable {

    private String id;
    private String prize;
    private String description;
    private String link;
    private String dtm;
    private String isNewFlat;

    public String getIsNewFlat() {
        return isNewFlat;
    }

    public void setIsNewFlat(String isNewFlat) {
        this.isNewFlat = isNewFlat;
    }


    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDtm() {
        return dtm;
    }

    public void setDtm(String dtm) {
        this.dtm = dtm;
    }
}



