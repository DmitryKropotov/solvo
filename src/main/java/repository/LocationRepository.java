package repository;

import models.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

public class LocationRepository {
    private Connection conn = ConnectionSaver.getConnection();
    private static int maxId = -1;

    {
        List<Location> loads =  this.select(new HashMap<>());
        OptionalInt maxId = loads.stream().mapToInt(location -> location.getId()).max();
        if (maxId.isPresent()) {
            this.maxId = maxId.getAsInt();
        }
    }

    public void create(String name) {
        PreparedStatement pstmt  = null;
        try {
            String sql = "INSERT INTO Location (id, name) VALUES (" + (maxId+1) +", '" + name + "');";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            maxId++;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Location with name " + name + " is created");
    }

    public void delete(int id) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM Location WHERE id = " + id + ";";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            System.out.println("Location with id = " + id + " is deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public List<Location> select(Map<String, Object> conditions) {
        PreparedStatement pstmt = null;
        List<Location> locations = new ArrayList();
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM Location");
            if (!conditions.isEmpty()) {
                sql.append(" WHERE ");
                List<String> keyList = Arrays.asList(conditions.keySet().toArray(new String[0]));
                for (int i = 0; i < keyList.size(); i++) {
                    String key = keyList.get(i);
                    Object value = conditions.get(key);
                    if(value instanceof String) {
                        sql.append(key).append("=").append("'").append(conditions.get(keyList.get(i))).append("'");
                    } else {
                        sql.append(keyList.get(i)).append("=").append(conditions.get(keyList.get(i)));
                    }
                    if (i != keyList.size() - 1) {
                        sql.append(" AND ");
                    }
                }
            }
            sql.append(";");
            pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                locations.add(new Location(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null){
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return locations;
    }

}
