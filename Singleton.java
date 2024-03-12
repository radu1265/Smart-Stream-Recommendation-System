package proiect;

import java.util.ArrayList;
import java.util.Iterator;

public class Singleton {
    private static Singleton instance = null;
    private ArrayList<Streamer> streamers = new ArrayList<>();
    private ArrayList<Stream> streams = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void addObjectToStreamers(Streamer object) {
        streamers.add(object);
    }

    public void addObjectToStreams(Stream object) {
        streams.add(object);
    }

    public void addObjectToUsers(User object) {
        users.add(object);
    }

    public ArrayList<Stream> getStreams() {
        return streams;
    }


    public Iterator<Streamer> getStreamerIterator() {
        return streamers.iterator();
    }

    public Iterator<Stream> getStreamIterator() {
        return streams.iterator();
    }

    public Iterator<User> getUserIterator() {
        return users.iterator();
    }

    public void deleteObjectFromStreams(Stream object) {
        streams.remove(object);
    }
}
