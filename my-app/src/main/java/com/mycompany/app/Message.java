package com.mycompany.app;

public class Message {

    private String sender_id;
    private String timestamp;
    private double dataX;
    private double dataY;
    private double dataZ;
    private double result;

    // Getter
    public String getSenderId() {
        return sender_id;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public double getDataX() {
        return dataX;
    }
    public double getDataY() {
        return dataY;
    }
    public double getDataZ() {
        return dataZ;
    }
    public double getResult() {
        return result;
    }



    // Setter
    public void setSenderId(String i) {
        this.sender_id = i;
    }
    public void setTimestamp(String t) {
        this.timestamp = t;
    }
    public void setDataX(double dx) {
        this.dataX = dx;
    }
    public void setDataY(double dy) {
        this.dataY = dy;
    }
    public void setDataZ(double dz) {
        this.dataZ = dz;
    }
    public void setResult(double r) {
        this.result = r;
    }


    //TO MAKE A SINGLE STRING OUT OF THE 'Message' OBJECT, TO BE ABLE TO PUBLISH A MESSAGE
    @Override
    public String toString() {
        return sender_id + "," + timestamp + "," + dataX + "," + dataY + "," + dataZ + "," + result ;
    }

    public String getString() {
        return toString();
    }
}


