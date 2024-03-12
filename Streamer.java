package proiect;

public class Streamer {
    private int streamerType;
    private int id;
    private String name;

    public Streamer(){}

    public Streamer(int streamerType, int id, String name) {
        this.streamerType = streamerType;
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public int getStreamerType() {
        return streamerType;
    }

    public String getName() {
        return name;
    }

}
