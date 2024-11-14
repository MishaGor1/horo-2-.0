package com.horo.horo.service;

import com.horo.horo.model.Horoscope;
import org.lightcouch.CouchDbClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.horo.horo.service.HoroscopeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;
import java.util.Arrays;
import java.util.List;
import com.horo.horo.config.DBConf;
import com.horo.horo.client.HoroClient;


// Реализация сервиса гороскопов
@Service
public class HoroscopeService implements HoroscopeServiceIn {

    private final RestTemplate restTemplate;
    private final CouchDbClient dbClient;

    @Autowired
    public HoroscopeService(RestTemplate restTemplate, CouchDbClient dbClient) {
        this.restTemplate = restTemplate;
        this.dbClient = dbClient;
    }

    @Override
    public void fetchAndSaveHoroscopes(List<String> signs, String type, String day) {
        for (String sign : signs) {
            fetchAndSaveHoroscope(sign, type, day);
        }
    }

    @Override
    public void fetchAndSaveHoroscope(String sign, String type, String day) {
        String url = String.format("https://any.ge/horoscope/api/?sign=%s&type=%s&day=%s&lang=en", sign, type, day);
        ResponseEntity<Horoscope[]> response = restTemplate.getForEntity(url, Horoscope[].class);
        Horoscope[] horoscopes = response.getBody();

        if (horoscopes != null && horoscopes.length > 0) {
            for (Horoscope horoscope : horoscopes) {
                dbClient.save(horoscope);
                System.out.println("Данные гороскопа сохранены: " + horoscope);
            }
        } else {
            System.out.println("Не удалось получить данные гороскопа для знака: " + sign);
        }
    }
}
