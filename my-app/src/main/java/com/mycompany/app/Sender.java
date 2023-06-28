package com.mycompany.app;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;


public class Sender {
    public void send(DbFunctions db , java.sql.Connection conn) throws IOException, TimeoutException //String message ,
    {
        //CREATE CONNECTION
        ConnectionFactory factory = new ConnectionFactory();

        try (Connection connection = factory.newConnection()){
            //CREATE CHANNEL "sender"
            Channel channel = connection.createChannel();
            channel.queueDeclare("sender", false, false, false, null);
            //CREATE CHANNEL "alert"
            Channel channelAlert = connection.createChannel();
            channelAlert.queueDeclare("alert", false, false, false, null);

            //READ JSON FILE AT THE FOLLOWING LOCAL ADDRESS "C:\\my-app\\src\\main\\java\\com\\mycompany\\app\\test.json"
            JSONParser parser = new JSONParser();
            try (Reader reader = new FileReader("C:\\my-app\\src\\main\\java\\com\\mycompany\\app\\test.json")) {

                //Read JSON file
                Object obj = parser.parse(reader);

                JSONArray messageList = (JSONArray) obj;
                System.out.println(messageList);

                messageList.forEach( message -> parseMessageObject( (Channel) channel, (Channel) channelAlert, (java.sql.Connection) conn, (DbFunctions) db, "sender_message_history", "alert_history"  ,(JSONObject) message ) );


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //CONFIRM MESSAGE IS SENT
            System.out.println("!!! message has been sent");

            //CLOSE CHANNELS
            channel.close();
            channelAlert.close();
        }
    }

    private static void parseMessageObject(Channel channel, Channel channelAlert, java.sql.Connection conn, DbFunctions db, String table_sender, String table_alert ,JSONObject message) {

        //CREATE OBJECT 'Message' FOR EACH "message" FROM JSON FILE
        JSONObject jsonObject = (JSONObject) message.get("message");
        Message m = new Message();

        //GET ID FROM JSON FILE
        m.setSenderId((String) jsonObject.get("sender_id"));

        //GET TIMESTAMP FROM JSON FILE
        m.setTimestamp((String) jsonObject.get("timestamp"));

        //GET DATA FROM JSON FILE
        JSONArray data = (JSONArray) jsonObject.get("data");
        Iterator<Double> iterator = data.iterator();
        m.setDataX(iterator.next());
        m.setDataY(iterator.next());
        m.setDataZ(iterator.next());

        //GET RESULT FROM JSON FILE
        m.setResult((double) jsonObject.get("result"));

        //CREATE A SINGLE STRING OUT OF THE 'Message' OBJECT
        String send = m.getString();

        //PUBLISH 'send' STRING IN CHANNEL 'sender' AND INSERT IN 'sender_message_history' TABLE IF 'result=1.0'
        try {
            //PUBLISH 'send' STRING IN CHANNEL "sender"
            channel.basicPublish("", "sender", false, null , send.getBytes());
            //CONFIRM MESSAGE IS SENT
            System.out.println("Sent : " + send);
            //INSERT 'Message' IN "sender_message_history" TABLE IN DATABASE
            db.insertRow(conn,table_sender, m.getSenderId() ,m.getTimestamp(), (float)m.getDataX(), (float)m.getDataY() , (float)m.getDataZ(), (float)m.getResult());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //PUBLISH 'send' STRING IN CHANNEL 'alert' AND INSERT IN 'alert_history' TABLE IF 'result=1.0'
        if (m.getResult()==1.0){
            try {
                //PUBLISH 'send' STRING IN CHANNEL "alert"
                channelAlert.basicPublish("", "alert", false, null , send.getBytes());
                //CONFIRM MESSAGE IS SENT
                System.out.println("Sent in alert : " + send);
                //INSERT 'Message' IN "alert_history" TABLE IN DATABASE
                db.insertRow(conn,table_alert, m.getSenderId() ,m.getTimestamp(), (float)m.getDataX(), (float)m.getDataY() , (float)m.getDataZ(), (float)m.getResult());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}


