package edu.agh.cmdUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DBFromJSONReader {

    final JSONParser parser = new JSONParser();
    private final JSONArray songsArray;
    private final JSONArray listenersArray;


    public DBFromJSONReader(String songsPath,String listenersPath) throws IOException, ParseException {
            FileReader songsReader = new FileReader(songsPath);
            FileReader listenersReader = new FileReader(listenersPath);
            songsArray = (JSONArray) parser.parse(songsReader);
            listenersArray = (JSONArray) parser.parse(listenersReader);
    }

    public Collection<String> getSongsParameters()
    {
        Collection<String> parameters=new ArrayList<>();
        for(Object songObject : songsArray) {
            StringBuilder commandBuilder = new StringBuilder();
            JSONObject song = (JSONObject) songObject;
            commandBuilder.append((String) song.get("name")).append(" ");
            commandBuilder.append((String) song.get("category")).append(" ");

            JSONArray artistsArray = (JSONArray) song.get("artists");
            for(Object artistObject : artistsArray) {
                JSONObject artist = (JSONObject) artistObject;
                commandBuilder.append((String) artist.get("name")).append(" ");
            }
            parameters.add(commandBuilder.toString());
        }
        return parameters;

    }

    public Collection<String> getListenersNames()
    {
        ArrayList<String> listenerNames=new ArrayList<>();
        for(Object listenerObject : listenersArray){
            JSONObject listener = (JSONObject) listenerObject;
            String name = (String) listener.get("name");
            listenerNames.add(name);
        }
        return listenerNames;
    }

    public Collection<String> getListenersViewedParameters()
    {
        Collection<String> viewedParams=new ArrayList<>();
        for(Object listenerObject:listenersArray)
        {
            StringBuilder commandBuilder = new StringBuilder();
            JSONObject listener = (JSONObject) listenerObject;
            String listenerName = (String) listener.get("name");

            JSONArray viewedSongs = (JSONArray) listener.get("viewed_songs");
            for(Object songObject : viewedSongs){
                commandBuilder.setLength(0);
                JSONObject song = (JSONObject) songObject;
                commandBuilder.append(listenerName).append(" ").append(song.get("name"));
                viewedParams.add(commandBuilder.toString());
            }
        }
        return viewedParams;
    }

    public Collection<String> getListenersLikedParameters()
    {
        Collection<String> likedParams=new ArrayList<>();
        for(Object listenerObject:listenersArray)
        {
            StringBuilder commandBuilder = new StringBuilder();
            JSONObject listener = (JSONObject) listenerObject;
            String listenerName = (String) listener.get("name");

            JSONArray likedSongs = (JSONArray) listener.get("liked_songs");
            for(Object songObject : likedSongs){
                JSONObject song = (JSONObject) songObject;
                commandBuilder.setLength(0);
                commandBuilder.append(listenerName).append(" ").append(song.get("name"));
            }
        }
        return likedParams;
    }
}
