package com.hazem.passwordbank.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 12/18/2015.
 */

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String name;


    @DatabaseField
    private String password;

    @DatabaseField
    private String specialNumber;

    @DatabaseField
    private String pattern;

    /* ////////////////////////////////////////////////////////////////////////// */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpecialNumber() {
        return specialNumber;
    }

    public void setSpecialNumber(String specialNumber) {
        this.specialNumber = specialNumber;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
