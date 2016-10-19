package com.dev.stdev.njuskalonovosti.classes;

import java.io.Serializable;

/**
 * Created by user on 3.10.2016..
 */

public class SearchClass implements Serializable {

    public String getGeneralId() {
        return generalId;
    }

    public void setGeneralId(String generalId) {
        this.generalId = generalId;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String generalId;
    private String search;
    private String type;





}
