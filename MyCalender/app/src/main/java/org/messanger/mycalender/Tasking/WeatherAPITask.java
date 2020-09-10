package org.messanger.mycalender.Tasking;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.messanger.mycalender.Data.Weather;
import org.messanger.mycalender.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherAPITask {
    private final static String apiKey= "hiding key";
    public Weather getWeather(double lon,double lat){
        Weather w=new Weather();
        String urlString="http://api.openweathermap.org/data/2.5/weather?"+"lat="+lat+"&lon="+lon+"&appid="+apiKey;

        try{
            URL url=new URL(urlString); //해당 문자열로 URL을 생성
            HttpURLConnection connection=(HttpURLConnection)url.openConnection(); //해당 커넥션을 열어 연결객체 생성
            InputStream inputStream=new BufferedInputStream(connection.getInputStream()); //인풋스트림객체를 받음
            JSONObject jsonObject=new JSONObject(getJsonString(inputStream)); //가져온 문자열을 제이슨으로 변경


            //필요한 값 파싱
            w.setTemperature(jsonObject.getJSONObject("main").getInt("temp"));
            w.setCity(jsonObject.getString("name"));
            w.setPressure(jsonObject.getJSONObject("main").getInt("pressure"));
            w.setWind(jsonObject.getJSONObject("wind").getDouble("speed"));
        }
        catch(MalformedURLException e){
            Log.d("MalformedURLException","URL Err");
            e.printStackTrace();
        }
        catch(IOException e){
            Log.d("IOException","Stream Err");
            e.printStackTrace();
        }
        catch(JSONException e){
            Log.d("JSONException","Can not parsing JSON");
            e.printStackTrace();
        }


        return w;
    }
    public String getJsonString(InputStream in){
        BufferedReader reader=null;
        StringBuilder builder=new StringBuilder();
        String bucket; //토막낸 문자열
        try{
            reader=new BufferedReader(new InputStreamReader(in));//in를 읽는 객체 (new InputStreamReader(in)) 를 담당하는 버퍼리더를 생성
            while((bucket=reader.readLine())!=null){ //가져올 문자열이 없을때까지
                builder.append(bucket);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        return builder.toString();
    }
}
