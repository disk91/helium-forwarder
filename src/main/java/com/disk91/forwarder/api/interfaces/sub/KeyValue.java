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

    public int size() {
        return entry.size();
    }

    public String getOneKey(String key) {
        if ( this.entry.get(key) != null ) {
            return this.entry.get(key).toString();
        }
        return null;
    }

    @Override
    public String toString() {
        boolean first = true;
        StringBuilder s = new StringBuilder("{");
        Iterator<Map.Entry<String, Object>> it = entry.entrySet().iterator();
        while (it.hasNext()) {
            if (!first) s.append(",");
            Map.Entry<String, Object> pair = it.next();
            s.append("\"").append(pair.getKey()).append("\" : \"").append(pair.getValue()).append("\"");
            first = false;
        }
        s.append("}");
        return s.toString();
    }
}
