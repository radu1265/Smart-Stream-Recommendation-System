package proiect;

import java.util.Iterator;

public class Stream {
    private int type;
    private int streamId;
    private int genre;
    private long noOfStreams;
    private int streamerId;
    private long length;
    private long dateAdded;
    private String name;

    public Stream(){}

    public Stream(int type, int id, int genre, long noOfStreams, int streamerId, long length, long dateAdded, String name) {
        this.type = type;
        this.streamId = id;
        this.genre = genre;
        this.noOfStreams = noOfStreams;
        this.streamerId = streamerId;
        this.length = length;
        this.dateAdded = dateAdded;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public int getStreamId() {
        return streamId;
    }

    public int getGenre() {
        return genre;
    }

    public long getNoOfStreams() {
        return noOfStreams;
    }

    public int getStreamerId() {
        return streamerId;
    }

    public long getLength() {
        return length;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public String getName() {
        return name;
    }
    public String getStreamerName(int streamerId) {
        Singleton singleton = Singleton.getInstance();
        Iterator<Streamer> streamerIterator = singleton.getStreamerIterator();
        while (streamerIterator.hasNext()) {
            Streamer streamer = streamerIterator.next();
            if (streamer.getId() == streamerId) {
                return streamer.getName();
            }
        }
        return null;
    }
    public void addNoOfStreams() {
        noOfStreams++;
    }
}