package com.postal.omniscient.postal.adapter;

/**
 * Created by Александр on 17.07.2016.
 */
public class AdapterDownloadFlag {
    private Boolean phoneRecIsWork = true;
    private Boolean treadIsWork = false;
    private Boolean dictaphoneIsWork = false;

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
    public void setPhoneRecIsWork(Boolean phoneRecIsWork) {
        this.phoneRecIsWork = phoneRecIsWork;
    }

    public Boolean getPhoneRecIsWork() {
        return phoneRecIsWork;
    }
}
