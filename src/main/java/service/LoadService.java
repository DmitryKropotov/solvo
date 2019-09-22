package service;

import models.Load;
import repository.LoadRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadService {
    private LoadRepository loadRepository = new LoadRepository();

    public void create(int locId) {
        loadRepository.create(locId);
    }

    public List<Load> select(Map<String, Object> conditions) {
        List<Load> result = loadRepository.select(conditions);
        return result;
    }

    public List<Load> selectById(int id) {
        Map<String, Object> conditions = new HashMap();
        conditions.put("id", id);
        List<Load> result = loadRepository.select(conditions);
        return result;
    }

}
