package com.dev.stdev.njuskalonovosti.classes;

import java.io.Serializable;

/**
 * Created by user on 6.10.2016..
 */

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
