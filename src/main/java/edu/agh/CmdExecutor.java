package edu.agh;

import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;
import edu.agh.services.ListenerService;
import edu.agh.services.SongService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdExecutor {
    private static final String ADD_SONG_CMD="add_song";
    private static final String ADD_ARTIST_CMD="add_artist";
    private static final String ADD_LISTENER_CMD="add_listener";
    private static final String GET_RECOMMENDATION_BY_CATEGORY_CMD="get_recommendation_by_category";
    private final SongService songService=new SongService();
    private final ListenerService listenerService=new ListenerService();

    //@TODO finish implementation, add commands and options
    //the command shoould look like, eg: add_song title category artist artist artist
    void execute(String cmd)
    {
        String[] words=cmd.split("\\s+");
        if(words[0].equalsIgnoreCase(ADD_SONG_CMD))
        {

        }
        else if(words[0].equalsIgnoreCase(ADD_ARTIST_CMD))
        {

        }
        else if(words[0].equalsIgnoreCase(ADD_LISTENER_CMD))
        {

        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_CATEGORY_CMD))
        {
            //@TODO just for fast tests - can be deleted
            //delete start
            List<Song> liked=new ArrayList<>();
            Song s=new Song("Hello");
            s.setCategory(Category.Blues);
            liked.add(s);
            Listener l1=new Listener("Konrad",liked,liked);
            listenerService.getRecommendationsByCategory(l1).forEach(song -> System.out.println(song.toString()));
            listenerService.getRecommendationsByCategory(l1).forEach(song -> System.out.println(song.toString()));
            listenerService.closeSession();//!!! this line is necessary in every command
            //delete end
        }
        else throw new IllegalArgumentException("No such command");

    }

    public List<String> getPossibleCommands()
    {
        return new ArrayList<String>(Arrays.asList(ADD_ARTIST_CMD, ADD_SONG_CMD, ADD_LISTENER_CMD, GET_RECOMMENDATION_BY_CATEGORY_CMD));
    }

    //@TODO method to inject the data from JSON to database
    public void getDataFromJson(final String path)
    {

    }
}
