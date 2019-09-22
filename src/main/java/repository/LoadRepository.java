package repository;

import models.Load;

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

public class LoadRepository {
    private Connection conn = ConnectionSaver.getConnection();
    private int maxId = -1;
    private List<String> names = new ArrayList<>();

    {
       List<Load> loads =  this.select(new HashMap<>());
       OptionalInt maxId = loads.stream().mapToInt(load -> load.getId()).max();
       if (maxId.isPresent()) {
           this.maxId = maxId.getAsInt();
       }
       loads.stream().map(load -> load.getName()).forEach(name -> {this.names.add(name);});
    }

    public void create(int locId) {
        PreparedStatement pstmt  = null;
        String name = generateName();
        try {
            String sql = "INSERT INTO Loads (id, name, Loc_id) VALUES (" + (maxId+1) +", '" + name + "', " + locId + ");";
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
        System.out.println("Load with name " + name + " is created");
    }

    public void delete(int id) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM Loads WHERE id = " + id +";";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            System.out.println("load with id = " + id + " is deleted");
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

    public List<Load> select(Map<String, Object> conditions) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Load> loads = new ArrayList();
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM Loads");
            if (!conditions.isEmpty()){
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
            rs = pstmt.executeQuery();
            while (rs.next()) {
                loads.add(new Load(rs.getInt("id"), rs.getString("name"), rs.getInt("Loc_id")));
            }
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
        return loads;
    }

    private String generateName() {
        int amountOfSymbols = (int)(Math.random()*24+1);
        if (amountOfSymbols == 24) {
            amountOfSymbols = 23;
        }
        String name = "";
        for (int i = 1; i < amountOfSymbols; i++) {
            int symbolNumber = (int)(Math.random()*62+1);
            if (symbolNumber == 63) {
                symbolNumber = 62;
            }
            final int CODE_OF_0 = 48, CODE_OF_UPPER_A = 65, CODE_OF_LOWER_A = 97;
            int charCode = (symbolNumber >= 1 && symbolNumber <= 10) ? CODE_OF_0 + symbolNumber - 1:
                    (symbolNumber >= 11 && symbolNumber <= 36) ? CODE_OF_UPPER_A + symbolNumber-10-1:
                            CODE_OF_LOWER_A + symbolNumber-36-1;
            name += (char)charCode;
        }
        if (names.contains(name)) {
            name = generateName();
        }
        return name;
    }
}
