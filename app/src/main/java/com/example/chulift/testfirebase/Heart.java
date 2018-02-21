package com.example.chulift.testfirebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chulift on 2/22/2018.
 */

@IgnoreExtraProperties
public class Heart {

    public int param1;
    public int param2;

    public Heart() {

    }

    public Heart(int param1, int param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("param1", param1);
        result.put("param2", param2);

        return result;
    }

    @Override
    public String toString() {
        return "{ param1:" + param1 + ", param2" + param2 + " }";
    }
}
