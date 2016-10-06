package com.dev.stdev.njuskalonovosti;

import java.io.Serializable;

/**
 * Created by user on 3.10.2016..
 */

public class pretrageClass implements Serializable {

    public String getGeneralId() {
        return generalId;
    }

    public void setGeneralId(String generalId) {
        this.generalId = generalId;
    }

    public String getPretraga() {
        return pretraga;
    }

    public void setPretraga(String pretraga) {
        this.pretraga = pretraga;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    private String generalId;
    private String pretraga;
    private String tip;





}
