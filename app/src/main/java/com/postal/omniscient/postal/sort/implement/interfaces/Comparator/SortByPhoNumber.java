package com.postal.omniscient.postal.sort.implement.interfaces.Comparator;

import com.postal.omniscient.postal.adapter.AdapterData;

import java.util.Comparator;

/**
 * Created by Alexandr on 28.04.2016.
 * Преопределяем Comparator для возможности сортировки
 * Collections.sort(sent_buf, new SortByPhoNumber());
 * в ReadContacts
 * сортировка по номеру и по дате
 */
public class SortByPhoNumber implements Comparator <AdapterData> {
    @Override
    public int compare(AdapterData ob1, AdapterData ob2) {
/**способ сортировки*/
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
