package service;

import models.Load;
import models.Location;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonService {
    private LoadService loadService = new LoadService();
    private LocationService locationService = new LocationService();

    public void createLoads(int numberOfLoads, String locationName) {
        Map<String, Object> condition = new HashMap();
        condition.put("name", locationName);
        List<Location> locations = locationService.select(condition);
        Location location = locations.isEmpty() ? locationService.createAndGet(locationName) : locations.get(0);
        for (int i = 0; i < numberOfLoads; i++) {
            loadService.create(location.getId());
        }
    }

    public void getInfoAboutLocations(List<String> locationNames) {
        Map<String, Object> condition = new HashMap();
        condition.put("name", locationNames);
        Map<String, List<Load>> locationNamesLoadMap = getLoadsByLocationNames(locationNames);
        locationNames.forEach(name ->{
            System.out.println("name of location is " + name + ", amount of loads is " + locationNamesLoadMap.get(name).size());
        });
    }

    public void createXmlFile(String nameOfFile) {
        List<Location> locations = locationService.select(new HashMap<>());
        List<String> locationNames =  locations.stream().map(Location::getName).collect(Collectors.toList());
        Map<String, List<Load>> locationNamesLoadMap = getLoadsByLocationNames(locationNames);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(nameOfFile+".xml"));
            writer.write("<dbInfo>"+'\n');
            for (Location location : locations) {
                try {
                    writer.write("<location name=\"" + location.getName() +
                            "\" id=\"" + location.getId() + "\">" + '\n');
                    for (Load load : locationNamesLoadMap.get(location.getName())) {
                        try {
                            writer.write("<load name=\"" + load.getName() + " id=\"" + load.getId() + "\"/>" + '\n');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    writer.write("</location>" + '\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writer.write("</dbInfo>");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, List<Load>> getLoadsByLocationNames(List<String> locationNames) {
        Map<String, List<Load>> result = new HashMap<>();
        for (String name : locationNames) {
            Map<String, Object> conditionForLocations = new HashMap<>();
            conditionForLocations.put("name", name);
            List<Location> locations = locationService.select(conditionForLocations);
            if (locations.size() == 0) {
                result.put(name, Collections.emptyList());
                continue;
            }
            Map<String, Object> conditionForLoads = new HashMap<>();
            conditionForLoads.put("Loc_id", locations.get(0).getId());
            result.put(name, loadService.select(conditionForLoads));
        }
        return result;
    }

}
