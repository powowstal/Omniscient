package com.postal.omniscient.postal.adapter;

/**
 * Created by Александр on 17.07.2016.
 */
public class AdapterDownloadFlag {
    public Boolean getTreadIsWork() {
        return treadIsWork;
    }

    public Boolean getDictaphoneIsWork() {
        return dictaphoneIsWork;
    }

    public void setTreadIsWork(Boolean treadIsWork) {
        this.treadIsWork = treadIsWork;
    }
    public void setDictaphoneIsWork(Boolean dictaphoneIsWork) {
        this.dictaphoneIsWork = dictaphoneIsWork;
    }

    private Boolean treadIsWork = false;
    private Boolean dictaphoneIsWork = false;


}
