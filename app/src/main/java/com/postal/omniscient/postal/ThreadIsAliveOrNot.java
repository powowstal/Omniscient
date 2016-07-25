package com.postal.omniscient.postal;

import java.util.Set;

/**
 * Created by Александр on 24.07.2016.
 */
public class ThreadIsAliveOrNot {
    public ThreadIsAliveOrNot(String threadName){
        this.threadName = threadName;
    }
    private String threadName;
    private Boolean live;

    public Boolean liveORnot(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        live = false;
        for(Thread th: threadSet){
            if(th.getName().equals(threadName)){
                live = true;
            }

        }
        return live;
    }
}
