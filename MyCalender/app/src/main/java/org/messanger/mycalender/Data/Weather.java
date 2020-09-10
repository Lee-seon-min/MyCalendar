package org.messanger.mycalender.Data;

public class Weather { //날씨 정보를 저장할 객체
    private int temperature;
    private String city;
    private int pressure;
    private double wind;

    public void setCity(String city) {
        this.city = city;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public int getPressure() {
        return pressure;
    }

    public int getTemperature() {
        return temperature;
    }

    public double getWind() {
        return wind;
    }

    public String getCity() {
        return city;
    }
}
