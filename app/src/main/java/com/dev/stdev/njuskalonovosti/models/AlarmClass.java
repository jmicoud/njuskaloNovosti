package com.dev.stdev.njuskalonovosti.models;

import java.io.Serializable;


public class AlarmClass implements Serializable{

    public String getGeneralid() {
        return generalid;
    }

    public void setGeneralid(String generalid) {
        this.generalid = generalid;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    private String generalid;
    private String interval;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    private String search;


}
