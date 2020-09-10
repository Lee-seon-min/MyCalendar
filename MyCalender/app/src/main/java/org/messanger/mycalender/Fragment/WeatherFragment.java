package org.messanger.mycalender.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.messanger.mycalender.Activity.MainActivity;
import org.messanger.mycalender.Data.Weather;
import org.messanger.mycalender.R;
import org.messanger.mycalender.Tasking.WeatherAPITask;

public class WeatherFragment extends Fragment { //날씨 API활용
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double longtitude , latitude;
    private TextView city,pres,temp,wind;
    private Weather weather;
    private Handler handler;
    final WeatherAPITask task=new WeatherAPITask();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.weather_fragment,container,false);
        setting(v);
        checkPermissions();

        new Thread(new Runnable() { //네트워크 태스킹을 할때는 다른 스레드를 사용해서 받아와야함.
            @Override
            public void run() {
                weather = task.getWeather(longtitude,latitude); //
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String loc = "위치 : "+weather.getCity()+"\n";
                        String pressure = "기압 : "+weather.getPressure()+"hPa\n";
                        String temperature = "온도 : "+Math.floor(weather.getTemperature()-273.15)+"℃\n";
                        String windy = "풍속 : "+weather.getWind()+"m/sec\n";
                        city.setText(loc);
                        pres.setText(pressure);
                        temp.setText(temperature);
                        wind.setText(windy);
                    }
                });
            }
        }).start();




        return v;
    }
    public void setting(View v) {
        city=v.findViewById(R.id.location);
        pres=v.findViewById(R.id.pres);
        temp=v.findViewById(R.id.temp);
        wind=v.findViewById(R.id.wind);
        handler=new Handler();
        locationManager=(LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE); //위치 서비스
        locationListener = new LocationListener() { //위치가 변할대 마다 작동하는 리스너
            @Override
            public void onLocationChanged(Location location) {
                longtitude = location.getLongitude();
                latitude = location.getLatitude();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        weather = task.getWeather(longtitude,latitude); //
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String loc = "위치 : "+weather.getCity()+"\n";
                                String pressure = "기압 : "+weather.getPressure()+"hPa\n";
                                String temperature = "온도 : "+Math.floor(weather.getTemperature()-273.15)+"℃\n";
                                String windy = "풍속 : "+weather.getWind()+"m/sec\n";
                                city.setText(loc);
                                pres.setText(pressure);
                                temp.setText(temperature);
                                wind.setText(windy);
                            }
                        });
                    }
                }).start();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };
    }
    public void checkPermissions(){ //위치정보를 가져오는것에 대한 동의를 물음
        if(Build.VERSION.SDK_INT>=26 && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            //위치정보를 가져오는것에 대한 권한 확인
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        else{
            Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //최근 위치를 받아온다.(LocationManager.NETWORK_PROVIDER 일때만 받아온다 이상하게...)
            longtitude=location.getLongitude(); //위도
            latitude=location.getLatitude(); //경도

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,locationListener); //1초마다 거리 1M씩 차이나면 갱신
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1,locationListener);
        }
    }
}
