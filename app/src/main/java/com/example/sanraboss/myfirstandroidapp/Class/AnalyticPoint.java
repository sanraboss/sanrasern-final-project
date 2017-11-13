package com.example.sanraboss.myfirstandroidapp.Class;

/**
 * Created by sanraboss on 11/13/17.
 */

public class AnalyticPoint {

    private float x;
    private float y;
    private String name;

    public AnalyticPoint(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        AnalyticPoint other = (AnalyticPoint) obj;
        if(other.getName() != this.name) return false;
        return true;
    }

    public String getName() {
        return name;
    }
}
