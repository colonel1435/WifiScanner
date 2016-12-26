package com.wumin.wifiscaner;


import java.io.Serializable;

/**
 * Created by Zero on 2016/09/21.
 */
public class WifiInfo implements Serializable{
    public String ssid = "";
    public String psw = "";

    @Override
    public String toString() {
        return "Wifi name : " + ssid + " password : " + psw + "\n";
    }
}
