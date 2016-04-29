package com.postal.omniscient.postal.contact.reader;

import android.content.ContentResolver;

/**
 * Created by Александр on 29.04.2016.
 * читаем ммс
 */
public class ReadMms {
    private ContentResolver contentResolver;
    private  String Msg = "MyMsg";

    public ReadMms (ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
}
