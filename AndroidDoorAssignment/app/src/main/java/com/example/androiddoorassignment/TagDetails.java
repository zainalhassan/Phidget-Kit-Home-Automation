package com.example.androiddoorassignment;
import android.app.Notification;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import androidx.appcompat.app.AppCompatActivity;



public class TagDetails extends AppCompatActivity {
    //    public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
//    private String userId = "17001340";
//    private String clientId = userId + "-sub";
//    private String rfidSensor = "/rfid";
//    private String motorSensor = "/motor";
//    private String door = "/c205";
//    private String rfidTopic = userId + rfidSensor;
//    private String motorTopic = clientId + motorSensor + door;
//
//    private MqttClient mqttClient;
//    Button publishMessageButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        System.out.println("I am tag details");
//        setContentView(tag_details);
//        System.out.println("I have read the xml file");
//        publishMessageButton = findViewById(R.id.openLock);
//        publishMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("Publishing the door to open the lock.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try
//                        {
//                            message();
//                        }
//                        catch (MqttException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//
//
//        try {
//            mqttClient = new MqttClient(BROKER_URL, clientId, null);
//            System.out.println("I am a mqtt client"+clientId);
//            mqttClient.setCallback(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable throwable) {
//
//                }
//
//                @Override
//                public void messageArrived(final String topic, MqttMessage mqttMessage) throws Exception {
//                    System.out.println("DEBUG: Message arrived. Topic: " + topic + "  Message: " + mqttMessage.toString());
//                    // get message data
//                    final String messageStr = mqttMessage.toString();
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            System.out.println("Updating UI");
//                            // Update UI elements
//                            if (rfidTopic.equals(topic)) {
//                                TextView brightnessVal = (TextView) findViewById(R.id.roomValue);
//                                brightnessVal.setText(messageStr);
//                            } else if ((motorTopic).equals(topic)) {
//                                TextView temperatureVal = (TextView) findViewById(R.id.tagValue);
//                                temperatureVal.setText(messageStr);
//                            }
//                        }
//                    });
//                    if ((rfidTopic + "LWT").equals(topic)) {
//                        System.err.println("RFID SENSOR - GONE");
//                    } else if ((motorTopic + "LWT").equals(topic)) {
//                        System.err.println("MOTOR SENSOR - GONE");
//                    }
//
//
//                }
//
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        try
//        {
//            startSubscribing();
//        }
//        catch (MqttException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void startSubscribing() throws MqttException {
//        mqttClient.connect();
//        final String topicRfid = rfidTopic;
//        mqttClient.subscribe(topicRfid);
//
//        System.out.println("Subscriber is now listening to " + topicRfid);
//
//        final String topicMotor = motorTopic;
//        mqttClient.subscribe(topicMotor);
//
//        System.out.println("Subscriber is now listening to " + topicMotor);
//
//
//    }
//
//    private void message() throws MqttException
//    {
//        mqttClient.connect();
//        System.out.println("CLIENT has Connected");
//        motorTopic = "17001340" + "-sub" + motorSensor + "/" + "c205";
//        final MqttTopic motTopic = mqttClient.getTopic(motorTopic);
//        System.out.println(motTopic);
//        System.out.println("SENDING THIS ROOM TO ECLIPSE" + "c205");
//        motTopic.publish(new MqttMessage("c205".getBytes()));
//        System.out.println("Published Data. Topic: "+ motTopic.getName() + " Message: " + "c205");
//    }
//
//}
    public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
    private String userid = "17106353";
    private String clientId = userid + "-android";
    private String motorSensor = "/motor";
    private String room = "";
    private String TOPIC_MOTOR = "";
    private String TOPIC_RFID = userid + "/rfid";
    private MqttClient mqttClient;

    Button openLock;

    TextView roomValue;

    // androidNotifications notification = new androidNotifications(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_tag);
        TextView tagValue = findViewById(R.id.tagValue);
        roomValue = findViewById(R.id.roomValue);
        System.out.println("hello this is tagDetails");
        try
        {
            Bundle extras = getIntent().getExtras();
            final androidData theAndroidData = (androidData) extras.get("dataObject");
            System.out.println("received from the intent: " + theAndroidData.getRoomId());
            tagValue.setText(theAndroidData.getTagId());
            roomValue.setText(theAndroidData.getRoomId());


            System.out.println("This is a room value " +  theAndroidData.getRoomId());
            room = theAndroidData.getRoomId();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        openLock = findViewById(R.id.openLock);
//        messageButton = findViewById(R.id.messagePage);
//        messageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewMessages();
//            }
//        });
        openLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Publishing topic");
                Toast.makeText(TagDetails.this, "Lock Opened.", Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            mqttMessages();
                            message();
                            startSubscribing();

                        }
                        catch (MqttException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });



    }

//    private void viewMessages() {
//        Intent intent = new Intent(this, viewMessages.class);
//        startActivity(intent);
//    }

    public void mqttMessages(){
        System.out.println("Connecting to the mqtt broker");
        try{

            mqttClient = new MqttClient(BROKER_URL, clientId, null);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
//                    try {
//                        mqttClient.reconnect();
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void messageArrived(final String topic, MqttMessage message) throws Exception {
                    System.out.println("I am message arrived");
                    System.out.println("DEBUG: MESSAGE ARRIVED... Topic: " + topic + "  Message: " + message);
                    final String messageString = message.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Updating.............");
//                            if(TOPIC_RFID.equals(topic)){
//                                TextView tagValue = findViewById(R.id.tagValue);
//                                tagValue.setText(messageString);
//                            }
                            if(TOPIC_MOTOR.equals(topic)){
                                TextView roomValue = findViewById(R.id.roomValue);
                                roomValue.setText(messageString);
                            }

                            room = roomValue.getText().toString();

                            String newTopic = room+"try/notifications";
                            if(newTopic.equals(topic)){
                                // notification.createNotification("Lock Notifications",messageString);
                            }
                        }
                    });
                    if((TOPIC_MOTOR+"/LWT").equals(topic)){
                        System.out.println("DEAD SENSOR");
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    private void startSubscribing() {
        try
        {
            //mqttClient.connect();
            final String topMotor = TOPIC_MOTOR;
            room = roomValue.getText().toString();

            String newTopic = room+"try/notifications";
            mqttClient.subscribe(topMotor);
            mqttClient.subscribe(newTopic);
            System.out.println("Subscriber is listening to" +topMotor);
        }
        catch (MqttSecurityException e)
        {
            e.printStackTrace();
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    private void message() throws MqttException
    {
        mqttClient.connect();
        System.out.println("CLIENT has Connected");
        TOPIC_MOTOR = userid + "-sub" + motorSensor + "/" + room;
        final MqttTopic motTopic = mqttClient.getTopic(TOPIC_MOTOR);
        System.out.println(motTopic);
        System.out.println("SENDING THIS ROOM TO ECLIPSE" + room);
        motTopic.publish(new MqttMessage(room.getBytes()));
        System.out.println("Published Data. Topic: "+ motTopic.getName() + " Message: " + room);
    }
}