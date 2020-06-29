package mqtt.Publisher;

import org.eclipse.paho.client.mqttv3.*;

public class PhidgetPublisher {
	public static final String BROKER_URL    = "tcp://broker.mqttdashboard.com:1883";
	public static final String userid        = "17106353";
	public static final String TOPIC_RFID    = userid + "/rfid";
	public static String TOPIC_MOTOR   = userid + "-sub" + "/motor" + "/";
	
	private MqttClient client;
	public PhidgetPublisher() {
		try 
		{
			client = new MqttClient(BROKER_URL, userid); // creates a Mqtt Session
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false); //both the client and server maintain their state across restarts of the client, the server and the connection.
			options.setWill(client.getTopic(userid + "/LWT"), "Conection Lost: ".getBytes(), 0, false);// if a client experience an unexpected connection loss to the server, the server will publish a message to itself using the supplied details.
			client.connect(options);
		}
		catch(MqttException me) 
		{
			me.printStackTrace();
			System.exit(1);
		}
	}


	public void publishMotor(double newMotorNum) throws MqttException {
        final MqttTopic motorTopic = client.getTopic(TOPIC_MOTOR);
        System.out.println("Publishing message : "+ newMotorNum + " to topic: "+ motorTopic.getName());
        final String motorMessage = newMotorNum + "";
        motorTopic.publish(new MqttMessage(motorMessage.getBytes()));
        System.out.println("Published data. Topic: " + motorTopic.getName() + "   Message: " + motorMessage);
    }
//	
//	public void publishMotor() throws MqttException {
//        final MqttTopic motorTopic = client.getTopic(TOPIC_MOTOR);
//        System.out.println("Publishing message : TESTER to topic: "+ motorTopic.getName());
//        final String motorMessage = "OPEN DOOR";
//        motorTopic.publish(new MqttMessage(motorMessage.getBytes()));
//        System.out.println("Published data. Topic: " + motorTopic.getName() + "   Message: " + motorMessage);
//    }
	
	public void publishMotor(String doorNumber) throws MqttException 
	{
		TOPIC_MOTOR = TOPIC_MOTOR + doorNumber;
        final MqttTopic motorTopic = client.getTopic(TOPIC_MOTOR);
        System.out.println("\nPublishing message : "+ doorNumber + " to topic: "+ motorTopic.getName());
        final String motorMessage = doorNumber + "TEST DOOR NUMBER";
        motorTopic.publish(new MqttMessage(motorMessage.getBytes()));
        System.out.println("Published data. Topic: " + motorTopic.getName() + "   Message: " + motorMessage +"\n");
    }

	public void publishRfid(String tagid) throws MqttException {
		final MqttTopic rfidTopic = client.getTopic(TOPIC_RFID);
		final String rfid = tagid + "";
		rfidTopic.publish(new MqttMessage(rfid.getBytes()));
		System.out.println("Published Data. Topic: " + rfidTopic.getName() + "   Message: " + rfid);
		
	}
	

}
