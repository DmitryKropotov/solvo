package models;

public class Load {
    private int id;
    private String name;
    private int locId;

    public Load(int id, String name, int locId) {
        this.id = id;
        this.name = name;
        this.locId = locId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLocId() {
        return locId;
    }
}
