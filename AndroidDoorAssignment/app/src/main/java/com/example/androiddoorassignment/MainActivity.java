package com.example.androiddoorassignment;
//zain al-hassan
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    androidData resultObject; //used to get a response back from the server and is stored in this object
    androidData androidInfo = new androidData("unknown", "unknown", "unknown", 0); // initializing
    Gson gson = new Gson();
    String fieldValue;
    EditText pinNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pinNumberView = findViewById(R.id.pinNumber);

        final Button submitButton = findViewById(R.id.sendPinNumber);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                System.out.println("Submit button pressed - Running the main method.");
                Toast.makeText(MainActivity.this, "Submit Button Pressed", Toast.LENGTH_SHORT).show();
                fieldValue = pinNumberView.getText().toString();
                System.out.println("FIELD VALUE: " + fieldValue);
                new SendJsonDataToServer().execute();
            }
        });
    }

    private String getServerResponse() throws IOException {
        URL objUrl = new URL("http://10.0.2.2:8080/MobileAppAssignmentServer/androidAppValidation?pinNumber="+fieldValue);
        HttpURLConnection con = (HttpURLConnection) objUrl.openConnection();
        con.setRequestMethod("GET");
        int serverResponse = con.getResponseCode();
        System.out.println("Server Response Code: " + serverResponse);
        if (serverResponse == HttpURLConnection.HTTP_OK)// exectues the code when the response is successful
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = convertStreamToString(in);
            System.out.println("This is the response from the server" + response);
            try
            {
                resultObject = gson.fromJson(response, androidData.class);
                System.out.println("Result Object: " + resultObject + "\n");
                try
                {
                    //setting the empty object to new values when the correct pin is found
                    androidInfo.setAndroidId(resultObject.getAndroidId());
                    androidInfo.setRoomId(resultObject.getRoomId());
                    androidInfo.setTagId(resultObject.getTagId());
                    androidInfo.setPinNumber(resultObject.getPinNumber());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                in.close();
                return response;
            } catch (JsonSyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String convertStreamToString(BufferedReader in) {
        java.util.Scanner js = new java.util.Scanner(in).useDelimiter("\\A");
        return js.hasNext() ? js.next() : "";
    }

    void startActivity() {
        Intent intent = new Intent(this, TagDetails.class);
        intent.putExtra("dataObject", resultObject);
        startActivity(intent);
    }

    private class SendJsonDataToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try
            {
                String response = getServerResponse();
                return response;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            if(!Objects.equals(response, ""))
            {
                System.out.println("I have received the response" + response);
                startActivity(); //takes a user to the new page
            }
            else
            {
                Toast.makeText(MainActivity.this, "An attempt to access the lock opener was made with an invalid pin. Please try again.", Toast.LENGTH_LONG).show();
            }
        }

    }
}