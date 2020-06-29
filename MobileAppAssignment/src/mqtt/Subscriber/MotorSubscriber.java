package mqtt.Subscriber;
//ZAIN AL-HASSAN 17106353
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;
import com.phidget22.PhidgetException;
import com.phidget22.RCServo;

import mqtt.Data.motorData;


public class MotorSubscriber 

{

	public static String sensorMotorURL = "http://localhost:8080/MobileAppAssignmentServer/ServerDB";
    public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
    public static final String userid = "17106353";


    static RCServo rcs;
    static Gson gson = new Gson();
    static motorData motorInfo = new motorData(0, "unknown");
    static String oneMotorJSON;
    static String roomName;
    String clientId = userid + "-sub" + "/motor" + "/";

    Connection conn = null;
    Statement stmt;
    ResultSet rs;

    private MqttClient mqttClient;
    public static void MotorId() throws PhidgetException 
    {
	  try
	  {
        rcs = new RCServo();
  		rcs.open(5000);
  		int id = rcs.getDeviceSerialNumber();
  		System.out.println("MOTOR-Serial Number " + id);
  		rcs.close();//rcs- connection closed.

  		motorData motordata = new motorData(0, "unknown");
  		motordata.setMotorid(id);
  	    String oneMotorJSON = gson.toJson(motordata);
  	    String result = sendToServer(oneMotorJSON); //sends the json string to the server
  	    System.out.println("\nJSON Format. Result: " + result);
  	    motorData resultObject = gson.fromJson(result, motorData.class);

  	  
  	    //setting the motorId and RoomId to the values received as a response fromt the server
  	    motorInfo.setMotorid(resultObject.getMotorid());
  	    motorInfo.setRoom(resultObject.getRoom());
  	    roomName = resultObject.getRoom();
  	    System.out.println("Room Number Received: " + roomName);
  	    System.out.println("Result Object: " + resultObject.toString() + "\n");

	  }

	  catch(PhidgetException pe)
	  {
	  	pe.getMessage();
	  }
	}

    public static String sendToServer(String oneMotorJSON)
    {
    	try 
    	{
    		System.out.println("\nBefore Encoding: " + oneMotorJSON);
    		oneMotorJSON = URLEncoder.encode(oneMotorJSON, "UTF-8");
			System.out.println("\nAfter Encoding: " + oneMotorJSON + "\n");
		}
    	catch (UnsupportedEncodingException uee) 
    	{
    		uee.printStackTrace();
		}
    	
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        
        String fullURL = sensorMotorURL+"?motorSerialNumber="+oneMotorJSON;
        System.out.println("Sending data to: " + fullURL);  // DEBUG confirmation message
        String line;
        String result = "";

        try 
        {
           url = new URL(fullURL);
           conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
           rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

           while ((line = rd.readLine()) != null) 
           {
              result += line;
              System.out.println("RESULT = " + result);
           }
           rd.close();
        } 
        catch (Exception e) 
        {
           e.printStackTrace();
        }
        return result;  
    }


	public MotorSubscriber() 
	{
        try 
        {	
        	mqttClient = new MqttClient(BROKER_URL, clientId);
        } 
        catch (MqttException me) 
        {
        	me.printStackTrace();
            System.exit(1);
        }
    }

    public void start() throws SQLException 

    {
        try 
        {
            mqttClient.setCallback(new MotorSubscribeCallback());
            mqttClient.connect();
            System.out.println("Subsriber's Status- CONNECTED");
            final String topic = clientId + roomName;
            mqttClient.subscribe(topic);
            System.out.println("Subscriber is now listening to " + topic);
        } 
        catch (MqttException me) 
        {

        	me.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String... args) throws PhidgetException, Exception 
    {
        final MotorSubscriber subscriber = new MotorSubscriber();
        MotorId();
        subscriber.start();
    }
}