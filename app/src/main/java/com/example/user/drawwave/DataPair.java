package com.example.user.drawwave;

/**
 * Created by user on 2018/5/19.
 */

public class DataPair {

    private double x;
    private double y;
    private double z;

    public DataPair()
    {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public void setValue(String X, String Y, String Z)
    {
        this.x = (Double.valueOf(X)).doubleValue();
        this.y = (Double.valueOf(Y)).doubleValue();
        this.z = (Double.valueOf(Z)).doubleValue();
    }

    public double getx()
    {
        return x;
    }

    public double gety()
    {
        return y;
    }

    public double getz()
    {
        return z;
    }

    public String debugx() { return String.valueOf(x); }

    public String debugy() { return String.valueOf(y); }

    public String debugz() { return String.valueOf(z); }

}
