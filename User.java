package proiect;

import java.util.List;

public class User {

    private int id;

    private String name;
    private List<Integer> streams;

    User(){}

    User(int id, String name, List<Integer> streams) {
        this.id = id;
        this.name = name;
        this.streams = streams;
    }
    public String getName() {
        return name;
    }

    public List<Integer> getStreams() {
        return streams;
    }

    public void addStream(int streamId) {
        streams.add(streamId);
    }

    public int getId() {
        return id;
    }
}
