package com.dev.stdev.njuskalonovosti;

import java.io.Serializable;

/**
 * Created by user on 6.10.2016..
 */

public class alarmClass implements Serializable{

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

    public String getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(String alarmid) {
        this.alarmid = alarmid;
    }

    private String generalid;
    private String interval;
    private String alarmid;



}
