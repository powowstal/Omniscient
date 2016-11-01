package com.postal.omniscient.postal;

import android.util.Log;
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
/**проверка запущен ли поток если да - live = true */
    public Boolean liveORnot(){
        try {
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            live = false;
            for (Thread th : threadSet) {
                if (th.getName().equals(threadName)) {
                    live = true;
                }

            }
        }catch (Exception e){
            Log.i("MyMsg","Error ThreadIsAliveOrNot liveORnot"+e);
        }
        return live;
    }
}
