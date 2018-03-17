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
    public int BPM;
    public String Emo;
    public int GSR;
    public long TIMESTAMP_C;
    public String Time;

    public Heart() {

    }

    public Heart(int bpm, String emo, int gsr, long timestamp, String time) {
        this.BPM = bpm;
        this.Emo = emo;
        this.GSR = gsr;
        this.TIMESTAMP_C = timestamp;
        this.Time = time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("BPM", BPM);
        result.put("Emo", Emo);
        result.put("GSR", GSR);
        result.put("TIMESTAMP_C", TIMESTAMP_C);
        result.put("Time", Time);

        return result;
    }

    @Override
    public String toString() {
        return this.BPM + ":" + this.GSR + ":" + this.Emo + ":" + this.TIMESTAMP_C + ":" + this.Time;
    }
}
