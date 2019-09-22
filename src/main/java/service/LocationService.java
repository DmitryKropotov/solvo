package service;

import models.Location;
import repository.LocationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationService {
    private LocationRepository locationRepository = new LocationRepository();

    public void create(String name) {
        locationRepository.create(name);
    }

    public Location createAndGet(String name) {
        create(name);
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", name);
        return select(conditions).get(0);
    }

    public List<Location> select(Map<String, Object> conditions) {
        List<Location> result = locationRepository.select(conditions);
        return result;
    }

    public Location selectById(int id) {
        Map<String, Object> conditions = new HashMap();
        conditions.put("id", id);
        List<Location> locationAsList = locationRepository.select(conditions);
        return locationAsList.isEmpty() ? null: locationAsList.get(0);
    }
}
