package com.mycompany.app;

import java.io.IOException;
import java.sql.Connection;
import java.util.concurrent.TimeoutException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, TimeoutException
    {


        //CONNECT TO DATABASE
        DbFunctions db= new DbFunctions();
        Connection conn = db.connect_to_db("tutodb", "postgres", "guest");

        //SEND AND RECEIVE MESSAGE
        Sender s = new Sender();
        Consumer c = new Consumer();
        //Send Message
        s.send(db , conn);
        //Receive Message
        c.receive(db , conn);


        //UNUSED FUNCTIONS -> CRUD POSTGRESQL DATABASE -------------------------------------------
        //CREATE TABLE
        //db.createTable(conn,"sender_message_history");
        //db.createTable(conn,"consumer_message_history");
        //db.createTable(conn,"alert_history");
        //READ DATA
        //db.readData(conn,"sender_message_history");
        //db.readData(conn,"consumer_message_history");
        //db.readData(conn,"alert_history");
        //DELETE TABLE
        //db.deleteTable(conn, "sender_message_history");
        //db.deleteTable(conn, "consumer_message_history");
        //db.deleteTable(conn, "alert_history");
    }
}
