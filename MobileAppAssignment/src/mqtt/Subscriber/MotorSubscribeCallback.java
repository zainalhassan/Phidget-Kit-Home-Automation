package mqtt.Subscriber;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import Utils.Utils;
import mqtt.Publisher.MotorMover;

public class MotorSubscribeCallback implements MqttCallback {

	public static final String user_id = "17106353";
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Message arrived.  Topic:  " + topic + "Message:  " + message.toString() + "\n");
		int move = 145;
		MotorMover.moveServoTo(move);
		Utils.waitFor(5);
		MotorMover.moveServoTo(0.0);
		Utils.waitFor(2);
		
		  if ((user_id+"/LWT").equals(topic)) 
		  {
	            System.err.println("Sensor gone!");
	      }
	    }

}


