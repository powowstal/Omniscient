package com.postal.omniscient.postal.sort;

/**
 * Created by Alexandr on 27.04.2016.
 */
import java.util.TreeSet;

public class Comp implements Comparable {

    public  String str;
    public String number;

    public Comp(String str, String number) {
        this.str = str;
        this.number = number;
    }

    @Override
    public int compareTo(Object obj) {
        Comp entry = (Comp) obj;

        int result = str.compareTo(entry.str);
        if(result != 0) {
            return result;
        }

//        result = number - entry.number;
        if(result != 0) {
            return (int) result / Math.abs( result );
        }
        return 0;
    }

}