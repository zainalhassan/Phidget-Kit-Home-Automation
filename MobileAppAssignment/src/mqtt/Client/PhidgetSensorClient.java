package mqtt.Client;

import org.eclipse.paho.client.mqttv3.MqttException;
import com.phidget22.*;
import mqtt.Publisher.PhidgetPublisher;

public class PhidgetSensorClient 
{
	RFID rfid = new RFID();
	PhidgetPublisher publisher = new PhidgetPublisher();
	
	public static void main(String[] args) throws PhidgetException 
	{
		new PhidgetSensorClient();
	}
	
	public PhidgetSensorClient() throws PhidgetException 
	{
		rfid.addTagListener(new RFIDTagListener() 
		{ 
			@Override
			public void onTag(RFIDTagEvent e) 
			{
				String tagRead = e.getTag();
				System.out.println("Debug: Punlishing rfid value over Mqtt: " + tagRead);
				System.out.println("Tag Read: " + tagRead); //prints as soon as a tag is read	
				try 
				{
					publisher.publishRfid(tagRead);
					publisher.publishMotor(180.0);
				}
				catch(MqttException mqtte) {
					mqtte.printStackTrace();
				}
			}
			
		});
		
		rfid.addTagLostListener(new RFIDTagLostListener() 
		{ 
			//listens for the tag lost
			@Override
			public void onTagLost(RFIDTagLostEvent e) 
			{
				String tagRead = e.getTag();
				System.out.println("Tag Lost: " + tagRead); //prints when a tag is lost
			}
		});
		
		rfid.open(5000); //gives 5 seconds for the device to respond
		rfid.setAntennaEnabled(true);//to make sure the antenna is turned on
		System.out.println("Device Name: " + rfid.getDeviceName()); //prints the device name
		System.out.println("Device Version: " + rfid.getDeviceVersion()); // prints the version
		System.out.println("Serial Number: " + rfid.getDeviceSerialNumber()); // prints the serial number
		
		System.out.println("\nGathering data for 20 seconds \n\n");
		try 
		{
			Thread.sleep(20000);//allows the phidget to stop listening after 20 seconds
		}
		catch(InterruptedException ie) 
		{ 
			ie.printStackTrace(); //prints more details about the error in the console
		}
		rfid.close(); //shuts the system once the code has run
		System.out.println("\n\nRFID Status: Closed");
	}

}
