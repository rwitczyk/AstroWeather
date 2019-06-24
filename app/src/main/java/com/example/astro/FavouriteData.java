package com.example.astro;

import io.realm.RealmObject;

public class FavouriteData extends RealmObject {
    String name;
    String idLocalization;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdLocalization() {
        return idLocalization;
    }

    public void setIdLocalization(String idLocalization) {
        this.idLocalization = idLocalization;
    }
}
