package com.example.springboot.kanban.kanban_app.dto;

public class WeatherDTO {

    private Integer temp;
    private Integer tempMin;
    private Integer tempMax;

    public WeatherDTO() {
    }

    public WeatherDTO(Integer temp, Integer tempMin, Integer tempMax) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getTempMin() {
        return tempMin;
    }

    public void setTempMin(Integer tempMin) {
        this.tempMin = tempMin;
    }

    public Integer getTempMax() {
        return tempMax;
    }

    public void setTempMax(Integer tempMax) {
        this.tempMax = tempMax;
    }

}
