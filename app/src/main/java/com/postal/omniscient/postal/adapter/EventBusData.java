package com.postal.omniscient.postal.adapter;

/**
 * Created by Александр on 22.07.2016.
 */
public class EventBusData {
    private final String comand;

    public EventBusData(String comand) {
        this.comand = comand;
    }

    public String getComand() {
        return comand;
    }
}
