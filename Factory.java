package proiect;

import java.util.List;

interface Factory {
    Streamer createStreamer(int streamerType, int id, String name);
    Stream createStream(int type, int id, int genre, long noOfStreams,
                        int streamerId, long length, long dateAdded, String name);
    User createUser(int id, String name, List<Integer> streams);
}
class StreamableFactory implements Factory {
    @Override
    public Streamer createStreamer(int streamerType, int id, String name) {
        return new Streamer(streamerType, id, name);
    }

    @Override
    public Stream createStream(int type, int id, int genre, long noOfStreams,
                               int streamerId, long length, long dateAdded, String name) {
        return new Stream(type, id, genre, noOfStreams, streamerId, length, dateAdded, name);
    }

    @Override
    public User createUser(int id, String name, List<Integer> streams) {
        return new User(id, name, streams);
    }
}
