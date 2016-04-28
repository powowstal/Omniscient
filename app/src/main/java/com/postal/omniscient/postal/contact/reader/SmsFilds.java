package com.postal.omniscient.postal.contact.reader;

/**
 * Created by Alexandr on 26.04.2016.
 */
public class SmsFilds {
    private String _id;
    private String _address;
    private String _msg;
    private String _time;


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


//    @Override
//    public int compareTo(SmsFilds obj) {
//        SmsFilds entry = (SmsFilds) obj;
//
//        int result = _address.compareTo(entry._address);
//        if(result != 0) {
//            return result;
//        }
//        result = _time.compareTo(entry._time);
//        if(result != 0) {
//            return result;
//        }
//        return 0;
//    }
}
