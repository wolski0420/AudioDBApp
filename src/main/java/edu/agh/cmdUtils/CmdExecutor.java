package edu.agh.cmdUtils;

import edu.agh.services.Neo4jSessionFactory;
import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;
import edu.agh.services.ArtistService;
import edu.agh.services.ListenerService;
import edu.agh.services.SongService;
import org.neo4j.ogm.session.Session;

import java.util.*;
import static edu.agh.cmdUtils.Commands.*;

public class CmdExecutor {
    private final SongService songService=new SongService();
    private final ListenerService listenerService=new ListenerService();
    private final ArtistService artistService = new ArtistService();

    public void execute(String cmd)
    {
        final String[] words=cmd.split("\\s+");

        if(words[0].equalsIgnoreCase(ADD_SONG_CMD))
        {
            if(words.length < 3) {
                System.out.println("Bad format! Type: " + ADD_SONG_CMD_WITH_OPTIONS);
                return;
            }

            songService.addNewSong(words[1],words[2],Arrays.copyOfRange(words,3,words.length));
            songService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(ADD_ARTIST_CMD))
        {
            if(words.length < 2) {
                System.out.println("Bad format! Type: " + ADD_ARTIST_CMD_WITH_OPTIONS);
                return;
            }

            artistService.addNewArtist(words[1]);
            artistService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(ADD_LISTENER_CMD))
        {
            if(words.length < 2) {
                System.out.println("Bad format! Type: " + ADD_LISTENER_CMD_WITH_OPTIONS );
                return;
            }

            listenerService.addNewListener(words[1]);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_CATEGORY_CMD))
        {
            if(words.length < 2){
                System.out.println("Bad format! Type: " + GET_RECOMMENDATION_BY_CATEGORY_CMD_WITH_OPTIONS);
                return;
            }

            listenerService.getRecommendationsByCategory(words[1]).forEach(System.out::println);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_ARTIST_CMD))
        {
            if(words.length < 2){
                System.out.println("Bad format! Type: " + GET_RECOMMENDATION_BY_ARTIST_CMD_WITH_OPTIONS);
                return;
            }

            listenerService.getRecommendationsByArtist(words[1]).forEach(System.out::println);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_SIMILAR_LISTENERS_CMD))
        {
            if(words.length < 2){
                System.out.println("Bad format! Type: " + GET_RECOMMENDATION_BY_SIMILAR_LISTENERS_CMD_WITH_OPTIONS);
                return;
            }

            listenerService.getRecommendationsBySimilarListeners(words[1]).forEach(System.out::println);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(LISTENER_LIKE_SONG_CMD))
        {
            if(words.length < 3){
                System.out.println("Bad format! Type: " + LISTENER_LIKE_SONG_CMD_WITH_OPTIONS);
                return;
            }

            listenerService.likeSong(words[1],words[2]);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(LISTENER_VIEWED_SONG_CMD))
        {
            if(words.length < 3){
                System.out.println("Bad format! Type: " + LISTENER_VIEWED_SONG_CMD_WITH_OPTIONS );
                return;
            }

            listenerService.viewedSong(words[1],words[2]);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(FIND_ARTISTS_BY_SONG_CMD)){
            if(words.length < 2){
                System.out.println("Bad format! Type: " + FIND_ARTISTS_BY_SONG_CMD_WITH_OPTIONS);
                return;
            }

            songService.findArtistsByTitle(words[1]).forEach(System.out::println);
            songService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(FIND_SONGS_BY_ARTIST_CMD)){
            if(words.length < 2){
                System.out.println("Bad format! Type: " + FIND_SONGS_BY_ARTIST_CMD_WITH_OPTIONS);
                return;
            }

            artistService.findSongsByArtistName(words[1]).forEach(System.out::println);
            artistService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(CLEAR)){
            Session session = Neo4jSessionFactory.getInstance().openNeo4jSession();
            session.query("MATCH (n) DETACH DELETE n",Collections.emptyMap());
            Neo4jSessionFactory.getInstance().closeSession();
        }
        else if(words[0].equalsIgnoreCase(QUIT_CMD))
        {
            System.exit(0);
        }
        else throw new IllegalArgumentException("No such command");
    }

    public List<String> getPossibleCommands()
    {
        return new ArrayList<>(Arrays.asList(ADD_ARTIST_CMD_WITH_OPTIONS,
                ADD_SONG_CMD_WITH_OPTIONS,
                ADD_LISTENER_CMD_WITH_OPTIONS,
                GET_RECOMMENDATION_BY_CATEGORY_CMD_WITH_OPTIONS,
                GET_RECOMMENDATION_BY_ARTIST_CMD_WITH_OPTIONS,
                GET_RECOMMENDATION_BY_SIMILAR_LISTENERS_CMD_WITH_OPTIONS,
                LISTENER_LIKE_SONG_CMD_WITH_OPTIONS,
                LISTENER_VIEWED_SONG_CMD_WITH_OPTIONS,
                FIND_ARTISTS_BY_SONG_CMD_WITH_OPTIONS,
                FIND_SONGS_BY_ARTIST_CMD_WITH_OPTIONS));
    }

    public void getDataFromJson(final String path)
    {
        DBFromJSONReader dbFromJSONReader;
        try
        {
            dbFromJSONReader=new DBFromJSONReader(path + "songs.json5",path + "listeners.json5");
            dbFromJSONReader.getSongsParameters().forEach((params)->this.execute(String.format("%s %s",ADD_SONG_CMD,params)));
            dbFromJSONReader.getListenersNames().forEach(name->this.execute(String.format("%s %s",ADD_LISTENER_CMD,name)));
            dbFromJSONReader.getListenersViewedParameters().forEach(parameters->this.execute(String.format("%s %s",LISTENER_VIEWED_SONG_CMD,parameters)));
            dbFromJSONReader.getListenersLikedParameters().forEach(parameters->this.execute(String.format("%s %s",LISTENER_LIKE_SONG_CMD,parameters)));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
