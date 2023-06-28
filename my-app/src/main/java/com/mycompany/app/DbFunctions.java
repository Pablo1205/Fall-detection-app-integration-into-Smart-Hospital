package com.mycompany.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbFunctions {

    public Connection connect_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }

    public void insertRow(Connection conn, String table_name, String senderId, String timestamp , float dataX, float dataY, float dataZ, float result){
        Statement statement;
        try{
            String query=String.format("insert into %s(senderID, timestamp, datax, datay, dataz, result) values ('%s' , '%s' , '%s' , '%s' , '%s' , '%s');", table_name, senderId,  timestamp, dataX, dataY, dataZ, result);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Inserted");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    //UNUSED FUNCTIONS -> CRUD POSTGRESQL DATABASE -------------------------------------------
    public void deleteTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query=String.format("drop table %s ", table_name);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void createTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query="create table "+table_name+" (mesId SERIAL, senderID varchar(200) ,timestamp varchar(200) , dataX float , dataY float , dataZ float , result float ,primary key(mesId));";
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void readData(Connection conn, String table_name){
        Statement statement;
        ResultSet rs = null;
        try{
            String query=String.format("select * from %s;", table_name);
            statement=conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()){
                System.out.println(rs.getString("mesId")+" ");
                System.out.println(rs.getString("senderId")+" ");
                System.out.println(rs.getString("timestamp ")+" ");
                System.out.println(rs.getFloat("dataX ")+" ");
                System.out.println(rs.getFloat("dataY ")+" ");
                System.out.println(rs.getFloat("dataZ ")+" ");
                System.out.println(rs.getFloat("result ")+" ");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
