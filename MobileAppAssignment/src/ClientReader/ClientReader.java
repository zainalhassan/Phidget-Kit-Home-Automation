package ClientReader;
//ZAIN AL-HASSAN 17106353
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;
import com.phidget22.*;

import mqtt.Data.rfidData;
import mqtt.Publisher.PhidgetPublisher;

public class ClientReader
{
	Gson gson = new Gson();
	RFID rfid = new RFID();
	rfidData rfidD = new rfidData("unknown", "unknown", 0, false);
	String oneRFIDJSON; 
	
	PhidgetPublisher publisher = new PhidgetPublisher();
	public static String sensorServerURL = "http://localhost:8080/MobileAppAssignmentServer/ServerDB";   
    
    public static void main(String[] args) throws PhidgetException 
    {
        new ClientReader();
    }
    


    public ClientReader() throws PhidgetException 
    {	
    	// Make the RFID Phidget able to detect loss or gain of an rfid card
        rfid.addTagListener(new RFIDTagListener() 
        {
			public void onTag(RFIDTagEvent e) {
				String tagRead = e.getTag();
				System.out.println("DEBUG: Tag read - and sending value: " + tagRead);
				try 
				{
					rfid.open();
					int readerid = rfid.getDeviceSerialNumber();
					rfidD.setReaderid(readerid);
					System.out.println("Reader Id: " + readerid);
					rfidD.setTagid(tagRead);
				}
				catch (PhidgetException pe) 
				{
					pe.printStackTrace();
				}
				oneRFIDJSON = gson.toJson(rfidD);
				String result = sendToServer(oneRFIDJSON);
				System.out.println("\nJSON: " + result);
				
				rfidData resultObject = gson.fromJson(result, rfidData.class);
				System.out.println("RESULTOBJECT: " + resultObject +"\n");
				
				if(resultObject.isValid())
				{
					try 
					{
						publisher.publishRfid(resultObject.getTagid());
						publisher.publishMotor(resultObject.getRoom());
					}
					catch (MqttException e1) 
					{
						e1.printStackTrace();
					}
					try
	    			{
	    				Thread.sleep(5000);
	    			}
	    			catch(InterruptedException ie)
	    			{
	    				ie.printStackTrace();
	    			}
					System.out.println("Tag Read: " + tagRead + ", ACCESS GRANTED, Interacting with the DOOR: " + resultObject.getRoom());
				}
				else
				{
					System.out.println("Tag Read: " + tagRead + ", ACCESS DENIED");
				}
			}
        });

        rfid.addTagLostListener(new RFIDTagLostListener() 
        {
			public void onTagLost(RFIDTagLostEvent e) 
			{
				System.out.println("DEBUG: Tag lost: " + e.getTag());
			}
        });
        
        rfid.open(5000);
        System.out.println("Serial Number " + rfid.getDeviceSerialNumber());
        try
        {                      
            System.out.println("\nGathering data for 20 seconds\n\n");
            pause(20);
            rfid.close();
            System.out.println("\nClosed RFID Reader");
            
        } 
        catch (PhidgetException pe) 
        {
            System.out.println(pe.getDescription());
        }
    }

    public String sendToServer(String oneRFIDJSON)
    {
    	try 
    	{
    		System.out.println("\nBefore Encoding: " + oneRFIDJSON);
			oneRFIDJSON = URLEncoder.encode(oneRFIDJSON, "UTF-8");
			System.out.println("After Encoding: " + oneRFIDJSON +"\n");
		}
    	catch(UnsupportedEncodingException uee) 
    	{
			uee.printStackTrace();
		}
    	
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String fullURL = sensorServerURL + "?readerdata=" + oneRFIDJSON;
        System.out.println("Sending data to: " + fullURL +"\n");
        String line;
        String result = "";
        try {
           url = new URL(fullURL);
           conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
           rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
           // Request response from server to enable URL to be opened
           while ((line = rd.readLine()) != null) 
           {
              result += line;
              System.out.println("The result = " + result +"\n");
           }
           rd.close();
        } 
        catch (Exception e) {
           e.printStackTrace();
        }
        return result;    	
    }
    
	private void pause(int secs)
	{
        try 
        {
			Thread.sleep(secs*1000);
		} 
        catch (InterruptedException ie) 
        {
			ie.printStackTrace();
		}

	}

}