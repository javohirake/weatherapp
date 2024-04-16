package org.w4;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataToDatabase {
    private static final String API_KEY="5ab83605c1d8040e01d4ae0ca4f0f0ee";
    private static final String urlDB="jdbc:postgresql://localhost:5432/OpenWeatherAPI";
    private static final String USER="postgres";
    private static final String password="root";


    private static JSONObject getWeatherDataFromAPI(String city) throws IOException {
        String APIurl="http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
        URL url=new URL(APIurl);
        HttpURLConnection connectionOpenWeather= (HttpURLConnection) url.openConnection();
        connectionOpenWeather.setRequestMethod("GET");

        BufferedReader reader=new BufferedReader(new InputStreamReader(connectionOpenWeather.getInputStream()));
        StringBuilder responce=new StringBuilder();
        String field=reader.readLine();

        while (field!=null){
            responce.append(field);
            field=reader.readLine();
        }
        reader.close();
        return new JSONObject(responce.toString());
    }
    private static void objectSavingIntoDB(JSONObject object) throws SQLException {
        Connection connectionDB= DriverManager.getConnection(urlDB, USER, password);
        String sqlQuery="insert into weather(city, temperature) values (?, ?, ?)";
        PreparedStatement statement=connectionDB.prepareStatement(sqlQuery);
        statement.setString(1, object.getString("name"));
        statement.setDouble(2, object.getJSONObject("main").getDouble("temp"));
        statement.setString(3, object.getJSONArray("weather")
                .getJSONObject(0).getString("description"));
        statement.execute();
        connectionDB.close();
    }

    public static void main(String[] args) throws IOException, SQLException {
        String city="Poole";
        JSONObject forecast=getWeatherDataFromAPI(city);
        objectSavingIntoDB(forecast);
    }
}
