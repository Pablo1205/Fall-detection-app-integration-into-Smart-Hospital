package com.mycompany.app;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static java.lang.Float.parseFloat;


public class Consumer {

    public void receive(DbFunctions db , java.sql.Connection conn) throws IOException, TimeoutException
    {
        //CREATE CONNECTION
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();

        //CONNECT TO CHANNEL "sender"
        Channel channel = connection.createChannel();
        channel.queueDeclare("sender", false, false, false, null);
        //CONNECT TO CHANNEL "alert"
        Channel channelAlert = connection.createChannel();
        channelAlert.queueDeclare("alert", false, false, false, null);

        //IF THERE ARE MESSAGES TO CONSUME IN CHANNEL "sender"
        channel.basicConsume("sender", true, (consumerTag, message) -> {
            //RECEIVE MESSAGE
            String m = new String(message.getBody(), "UTF-8");
            System.out.println("I just received a message = " + m);
            //SPLIT MESSAGE TO INSERT IN "consumer_message_history" TABLE IN DATABASE
            String[] content = m.split(",");
            db.insertRow(conn,"consumer_message_history", content[0], content[1], parseFloat(content[2]), parseFloat(content[3]) , parseFloat(content[4]), parseFloat(content[5]));
        }, consumerTag -> {});

        //IF THERE ARE MESSAGES TO CONSUME IN CHANNEL "alert" -> nothing is done, just a terminal display and a way to empty the channel 'alert'
        channelAlert.basicConsume("alert", true, (consumerTag, message) -> {
            //RECEIVE MESSAGE
            String m = new String(message.getBody(), "UTF-8");
            System.out.println("I just received a message FROM ALERT= " + m);
        }, consumerTag -> {});

        //CONFIRM MESSAGE IS SENT
        System.out.println("!!! messages received");
    }
}
