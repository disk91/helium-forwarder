package com.disk91.forwarder.api.interfaces.sub;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KeyValue {

    @JsonIgnore
    private Map<String, Object> entry = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getEntry() {
        return entry;
    }

    @JsonAnySetter
    public void setEntry(String name, Object value) {
        this.entry.put(name, value);
    }

    @Override
    public String toString() {
        boolean first = true;
        String s = "{";
        Iterator it = entry.entrySet().iterator();
        while (it.hasNext()) {
            if (!first) s += ",";
            Map.Entry pair = (Map.Entry) it.next();
            s += "\"" + pair.getKey() + "\" : \"" + pair.getValue() + "\"";
            first = false;
        }
        s += "}";
        return s;
    }
}
