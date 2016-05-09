package com.postal.omniscient.postal.adapter;

/**
 * Адаптер
 * Содержит поля, в которые записуются данные сетерами
 * и получаются гетерамми
 * Created by Alexandr on 26.04.2016.
 */
public class AdapterData {
    private String _id;
    private String _address;
    private String _msg;
    private String _time;
    private String _index_folder_name;

    public String getId(){
        return _id;
    }
    public String getAddress(){
        return _address;
    }
    public String getMsg(){
        return _msg;
    }
    public String getTime(){
        return _time;
    }
    public String get_index_folder_name() {
        return _index_folder_name;
    }

    public void setId(String id){
        _id = id;
    }
    public void setAddress(String address){
        _address = address;
    }
    public void setMsg(String msg){
        _msg = msg;
    }
    public void setTime(String time){
        _time = time;
    }
    public void set_index_folder_name(String index_folder_name) {
        this._index_folder_name = _index_folder_name;
    }



}
