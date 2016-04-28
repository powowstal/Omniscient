package com.postal.omniscient.postal.sort.implement.interfaces.Comparator;

import com.postal.omniscient.postal.contact.reader.SmsFilds;

import java.util.Comparator;

/**
 * Created by Alexandr on 28.04.2016.
 */
public class SortByPhoNumber implements Comparator <SmsFilds> {
    @Override
    public int compare(SmsFilds ob1, SmsFilds ob2) {

        int result = ob1.getAddress().compareTo(ob2.getAddress());
        if(result != 0) {
            return result;
        }
        result = ob1.getTime().compareTo(ob2.getTime());
        if(result != 0) {
            return result;
        }
        return 0;
    }
}
