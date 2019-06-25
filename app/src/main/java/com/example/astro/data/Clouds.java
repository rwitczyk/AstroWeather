
package com.example.astro.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Clouds  implements Serializable {

    private Integer all;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
