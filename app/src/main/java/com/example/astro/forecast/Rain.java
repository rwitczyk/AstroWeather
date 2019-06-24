
package com.example.astro.forecast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Rain implements Serializable
{

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -4506807789215948584L;

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
