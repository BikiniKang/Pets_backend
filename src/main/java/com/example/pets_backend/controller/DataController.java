package com.example.pets_backend.controller;

import com.example.pets_backend.entity.City;
import com.example.pets_backend.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class DataController {

    private final CityRepository cityRepository;

    @PostMapping("/data/location_list")
    public void postLocationList(@RequestBody List<String> list) {
        for (String name : list) {
            City city = new City();
            city.setName(name);
            cityRepository.save(city);
        }
    }

    @GetMapping("/data/location_list")
    public List<City> getLocationList() {
        return cityRepository.findAll();
    }
}
