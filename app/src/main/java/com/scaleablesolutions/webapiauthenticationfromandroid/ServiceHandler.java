package com.scaleablesolutions.webapiauthenticationfromandroid;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ScaleableSolutions on 20/1/2016.
 */
public class ServiceHandler {

    private final static String URL = Constants.SERVICE_URL + "/api/data/v8.0/";

    public String whoAmIRequest(String token) {
        String DestinationURL = URL + "WhoAmI";
        String response = GetRequest(DestinationURL, token);
        return response;
    }

    public String userInfoRequest(String token, String userId) {
        String DestinationURL = URL + "systemusers(" + userId + ")?$select=fullname";
        String response = GetRequest(DestinationURL, token);
        return response;
    }

    private String GetRequest(String destinationURL, String token) {
        String response = "";
        try {
            URL LoginURL = new URL(destinationURL);
            HttpURLConnection rc = (HttpURLConnection) LoginURL.openConnection();

            rc.setRequestMethod("GET");

            rc.setRequestProperty("OData-MaxVersion", "4.0");
            rc.setRequestProperty("OData-Version", "4.0");
            rc.setRequestProperty("Accept", "application/json");
            rc.setRequestProperty("Authorization", "Bearer " + token);

            rc.connect();

            InputStreamReader read = new InputStreamReader(rc.getInputStream());
            StringBuilder sb = new StringBuilder();
            int ch = read.read();
            while (ch != -1) {
                sb.append((char) ch);
                ch = read.read();
            }

            response = sb.toString();
            read.close();
            rc.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
