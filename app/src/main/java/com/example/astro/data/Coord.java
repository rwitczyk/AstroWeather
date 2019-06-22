
package com.example.astro.data;

import java.util.HashMap;
import java.util.Map;

public class Coord {

    private Integer lon;
    private Integer lat;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getLon() {
        return lon;
    }

    public void setLon(Integer lon) {
        this.lon = lon;
    }

    public Integer getLat() {
        return lat;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
