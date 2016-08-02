package com.postal.omniscient.postal.adapter;

/**
 * Created by Александр on 02.08.2016.
 */
public class EventBusCall {
    private final String comand;

    public EventBusCall(String comand) {
        this.comand = comand;
    }

    public String getComand() {
        return comand;
    }
}
