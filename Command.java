package proiect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

interface Command {
    void read(String[] args);
    void listUser(User user);
    void listStreamer(Streamer streamer);
    void addStream(Streamer streamer, String[] buffer);
    void deleteStream(Streamer streamer, Stream stream);
    void listenStream(User user, Stream stream);
    void recommendStream(User user, String type);
    void surpriseStream(User user, String type);
    void build(String[] args);
}

class SaveCommand implements Command {

    public static String date(long datetoconvert) {
        Date date = new Date(datetoconvert * 1000);
        SimpleDateFormat simple = new SimpleDateFormat("dd-MM-yyyy");
        simple.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simple.format(date);
    }

    public static String length(long length) {
        long hours = length / 3600;
        long minutes = (length % 3600) / 60;
        long seconds = length % 60;
        if (hours == 0) {
            return String.format("%02d:%02d", minutes, seconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    @Override
    public void read(String[] args) {
        Singleton singleton = Singleton.getInstance();
        Factory factory = new StreamableFactory();
        //read streamer
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + args[0]))) {
            String line;
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] buffer = line.split(",");
                //take every buffer without first and last character
                for(int i = 0; i < buffer.length; i++) {
                    buffer[i] = buffer[i].substring(1, buffer[i].length() - 1);
                }
                Streamer streamer = factory.createStreamer(Integer.parseInt(buffer[0]), Integer.parseInt(buffer[1]), buffer[2]);
                singleton.addObjectToStreamers(streamer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //read stream
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + args[1]))) {
            String line;
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] buffer = line.split("\",\"");
                buffer[0] = buffer[0].substring(1);
                buffer[buffer.length - 1] = buffer[buffer.length - 1].substring(0, buffer[buffer.length - 1].length() - 1);
                long length = Long.parseLong(buffer[5]);
                long date = Long.parseLong(buffer[6]);
                Stream stream = factory.createStream(Integer.parseInt(buffer[0]), Integer.parseInt(buffer[1]), Integer.parseInt(buffer[2]),
                        Long.parseLong(buffer[3]), Integer.parseInt(buffer[4]), length, date, buffer[7]);
                singleton.addObjectToStreams(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //read user
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + args[2]))) {
            String line;
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] buffer = line.split("\",\"");
                buffer[0] = buffer[0].substring(1);
                buffer[buffer.length - 1] = buffer[buffer.length - 1].substring(0, buffer[buffer.length - 1].length() - 1);
                List<Integer> streams = Arrays.asList(buffer[2].split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());
                User user = factory.createUser(Integer.parseInt(buffer[0]), buffer[1], streams);
                singleton.addObjectToUsers(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void listUser(User user) {

        List<Integer> streams = user.getStreams();
        System.out.print("[");
        for (Integer integer : streams) {
            List<Stream> streamList = Singleton.getInstance().getStreams();
            Stream stream = streamList.stream().filter(x -> x.getStreamId() == integer).findFirst().orElse(null);

            System.out.print("{");
            System.out.print("\"id\":\"" + stream.getStreamId() + "\",");
            System.out.print("\"name\":\"" + stream.getName() + "\",");
            System.out.print("\"streamerName\":\"" + stream.getStreamerName(stream.getStreamerId()) + "\",");
            System.out.print("\"noOfListenings\":\"" + stream.getNoOfStreams() + "\",");
            System.out.print("\"length\":\"" + length(stream.getLength()) + "\",");
            System.out.print("\"dateAdded\":\"" + date(stream.getDateAdded()) + "\"");
            System.out.print("}");
            if (streams.indexOf(integer) != streams.size() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("]");

    }
    public void listStreamer(Streamer streamer) {
        //get all streams form streamer
        //search for streams using streamer id
        List<Stream> streams = Singleton.getInstance().getStreams();
        List<Stream> streamList = streams.stream().filter(x -> x.getStreamerId() == streamer.getId()).collect(Collectors.toList());
        System.out.print("[");
        for (Stream stream : streamList) {
            System.out.print("{");
            System.out.print("\"id\":\"" + stream.getStreamId() + "\",");
            System.out.print("\"name\":\"" + stream.getName() + "\",");
            System.out.print("\"streamerName\":\"" + stream.getStreamerName(stream.getStreamerId()) + "\",");
            System.out.print("\"noOfListenings\":\"" + stream.getNoOfStreams() + "\",");
            System.out.print("\"length\":\"" + length(stream.getLength()) + "\",");
            System.out.print("\"dateAdded\":\"" + date(stream.getDateAdded()) + "\"");
            System.out.print("}");
            if (streamList.indexOf(stream) != streamList.size() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("]");

    }
    public void addStream(Streamer streamer, String[] buffer) {
        Factory factory = new StreamableFactory();
        LocalDate date = LocalDate.of(2023, 1, 13);
        long epochSecond = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        //create stream
        Stream stream = factory.createStream(Integer.parseInt(buffer[0]), Integer.parseInt(buffer[1])
                , Integer.parseInt(buffer[2]),0, streamer.getId(), Integer.parseInt(buffer[3]), epochSecond,buffer[4]);
        //add stream to streams
        Singleton.getInstance().addObjectToStreams(stream);
    }
    public void deleteStream(Streamer streamer, Stream stream) {
        Singleton.getInstance().deleteObjectFromStreams(stream);
    }
    public void listenStream(User user, Stream stream) {
        //add stream id to user streams
        user.addStream(stream.getStreamId());
        //add 1 to stream noOfStreams
        stream.addNoOfStreams();
    }
    @Override
    public void recommendStream(User user, String type) {
        int streamType = 0;
        switch (type) {
            case "SONG":
                streamType = 1;
                listStreamsTop5(user.getStreams(), streamType);
                break;
            case "PODCAST":
                streamType = 2;
                listStreamsTop5(user.getStreams(), streamType);
                break;
            case "AUDIOBOOK":
                streamType = 3;
                listStreamsTop5(user.getStreams(), streamType);
                break;
            default:
                System.out.println("Invalid type");
        }
    }
    // top 5 most listened streams of a given type
    public void listStreamsTop5(List<Integer> userStreams, int streamType) {
        List<Stream> streams = Singleton.getInstance().getStreams();
        List<Stream> streamList = streams.stream().filter(x -> x.getType() == streamType).collect(Collectors.toList());
        List<Integer> streamerIds = new ArrayList<>();
        //streamers list
        for (Integer streamID : userStreams) {
            for (Stream stream : streamList) {
                if (stream.getStreamId() == streamID) {
                    streamerIds.add(stream.getStreamerId());
                }
            }
        }
        List<Stream> streamList1 = streamList.stream().filter(x -> !userStreams.contains(x.getStreamId())).collect(Collectors.toList());
        streamList1 = streamList1.stream().filter(x -> streamerIds.contains(x.getStreamerId())).collect(Collectors.toList());
        streamList1.sort(Comparator.comparing(Stream::getNoOfStreams).reversed());
        System.out.print("[");
        //print first 5, if there are less than 5 print all
        for (int i = 0; i < streamList1.size() && i < 5; i++) {
            System.out.print("{");
            System.out.print("\"id\":\"" + streamList1.get(i).getStreamId() + "\",");
            System.out.print("\"name\":\"" + streamList1.get(i).getName() + "\",");
            System.out.print("\"streamerName\":\"" + streamList1.get(i).getStreamerName(streamList1.get(i).getStreamerId()) + "\",");
            System.out.print("\"noOfListenings\":\"" + streamList1.get(i).getNoOfStreams() + "\",");
            System.out.print("\"length\":\"" + length(streamList1.get(i).getLength()) + "\",");
            System.out.print("\"dateAdded\":\"" + date(streamList1.get(i).getDateAdded()) + "\"");
            System.out.print("}");
            if (i != 4 && i != streamList1.size() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("]");
    }
    public void surpriseStream(User user, String type) {
        int streamType = 0;
        switch (type) {
            case "SONG":
                streamType = 1;
                listSurprise(user.getStreams(), streamType);
                break;
            case "PODCAST":
                streamType = 2;
                listSurprise(user.getStreams(), streamType);
                break;
            case "AUDIOBOOK":
                streamType = 3;
                listSurprise(user.getStreams(), streamType);
                break;
            default:
                System.out.println("Invalid type");
        }
    }

    public void listSurprise(List<Integer> userStreams, int streamType) {
        List<Stream> streams = Singleton.getInstance().getStreams();
        List<Stream> streamList = streams.stream().filter(x -> x.getType() == streamType).collect(Collectors.toList());
        List<Integer> streamerIds = new ArrayList<>();
        //streamers list
        for (Integer streamID : userStreams) {
            for (Stream stream : streamList) {
                if (stream.getStreamId() == streamID) {
                    streamerIds.add(stream.getStreamerId());
                }
            }
        }
        List<Stream> streamList1 = streamList.stream().filter(x -> !userStreams.contains(x.getStreamId())).collect(Collectors.toList());
        streamList1 = streamList1.stream().filter(x -> !streamerIds.contains(x.getStreamerId())).collect(Collectors.toList());
        //sort by date added newest first
        // if date added is the same sort by no of streams
        streamList1.sort(Comparator.comparing(Stream::getDateAdded).reversed());
        for (int i = 0; i < streamList1.size(); i++) {
            for (int j = i + 1; j < streamList1.size(); j++) {
                if (streamList1.get(i).getDateAdded() == streamList1.get(j).getDateAdded()) {
                    if (streamList1.get(i).getNoOfStreams() < streamList1.get(j).getNoOfStreams()) {
                        Stream temp = streamList1.get(i);
                        streamList1.set(i, streamList1.get(j));
                        streamList1.set(j, temp);
                    }
                }
            }
        }
        System.out.print("[");
        //print first 3, if there are less than 5 print all
        for (int i = 0; i < streamList1.size() && i < 3; i++) {
            System.out.print("{");
            System.out.print("\"id\":\"" + streamList1.get(i).getStreamId() + "\",");
            System.out.print("\"name\":\"" + streamList1.get(i).getName() + "\",");
            System.out.print("\"streamerName\":\"" + streamList1.get(i).getStreamerName(streamList1.get(i).getStreamerId()) + "\",");
            System.out.print("\"noOfListenings\":\"" + streamList1.get(i).getNoOfStreams() + "\",");
            System.out.print("\"length\":\"" + length(streamList1.get(i).getLength()) + "\",");
            System.out.print("\"dateAdded\":\"" + date(streamList1.get(i).getDateAdded()) + "\"");
            System.out.print("}");
            if (i != 2 && i != streamList1.size() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("]");

    }
    public void build (String[] args) {

        Command command = new SaveCommand();
        command.read(args);
        //read commands form args[3]
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + args[3]))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = null;
                boolean userOrStreamer = false;
                String[] buffer = line.split(" ");
                //check if buffer[0] is equal to the id of a User
                Iterator<User> userIterator = Singleton.getInstance().getUserIterator();
                while (userIterator.hasNext()) {
                    user = userIterator.next();
                    if (user.getId() == Integer.parseInt(buffer[0])) {
                        //this is the user that will be used for the command
                        userOrStreamer = true;
                        break;
                    }
                }
                //the users commands
                if(userOrStreamer) {
                    switch (buffer[1]) {
                        case "LIST":
                            command.listUser(user);
                            break;
                        case "LISTEN":
                            //check if stream exists
                            Iterator<Stream> streamIterator = Singleton.getInstance().getStreamIterator();
                            Stream stream = null;
                            boolean streamExists = false;
                            while (streamIterator.hasNext()) {
                                stream = streamIterator.next();
                                if (stream.getStreamId() == Integer.parseInt(buffer[2])) {
                                    //this is the stream that will be used for the command
                                    streamExists = true;
                                    break;
                                }
                            }
                            if (streamExists) {
                                command.listenStream(user, stream);
                            } else {
                                System.out.println("Stream does not exist");
                            }
                            break;
                        case "RECOMMEND":
                            command.recommendStream(user, buffer[2]);
                            break;
                        case "SURPRISE":
                            command.surpriseStream(user, buffer[2]);
                            break;

                        default:
                            System.out.println("Invalid command");
                    }
                    continue;
                }
                Streamer streamer = null;
                if(!userOrStreamer) {
                    Iterator<Streamer> streamerIterator = Singleton.getInstance().getStreamerIterator();
                    while (streamerIterator.hasNext()) {
                        streamer = streamerIterator.next();
                        if (streamer.getId() == Integer.parseInt(buffer[0])) {
                            //this is the user that will be used for the command
                            userOrStreamer = true;
                            break;
                        }
                    }
                }

                if (userOrStreamer) {
                    //the streamers commands
                    switch (buffer[1]) {
                        case "LIST":
                            command.listStreamer(streamer);
                            break;
                        case "ADD":
                            String[] buffer2 = new String[buffer.length - 2];
                            for (int i = 2; i < buffer.length; i++) {
                                if (i < 7)
                                    buffer2[i - 2] = buffer[i];
                                else
                                    buffer2[4] = buffer2[4] + " " + buffer[i];
                            }
                            command.addStream(streamer, buffer2);
                            break;
                        case "DELETE":
                            //check if stream has streamer id
                            List<Stream> streams = Singleton.getInstance().getStreams();
                            for (Stream stream : streams) {
                                if (stream.getStreamerId() == Integer.parseInt(buffer[2]) && stream.getStreamerId() == streamer.getId()) {
                                    command.deleteStream(streamer, stream);
                                    break;
                                }
                            }
                            break;
                        default:
                            System.out.println("Invalid command");
                    }
                }

                if (!userOrStreamer) {
                    System.out.println("Invalid command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}