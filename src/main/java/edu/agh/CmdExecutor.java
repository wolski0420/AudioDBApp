package edu.agh;

import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;
import edu.agh.services.ArtistService;
import edu.agh.services.ListenerService;
import edu.agh.services.SongService;
import org.neo4j.ogm.session.Session;
import java.util.*;
import static edu.agh.Commands.*;

public class CmdExecutor {
    private final SongService songService=new SongService();
    private final ListenerService listenerService=new ListenerService();
    private final ArtistService artistService = new ArtistService();

    //the command should look like, eg: add_song title category artist artist artist
    void execute(String cmd)
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
                System.out.println("Bad format! Tyep: " + FIND_SONGS_BY_ARTIST_CMD_WITH_OPTIONS);
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
        else if(words[0].equalsIgnoreCase(TEST_CMD))
        {
            //execute("clear");
            Artist a1=new Artist("Adele");
            Artist a2=new Artist("Madele");
            Song s1=new Song("Hello",Category.Blues,Arrays.asList(a1,a2));
            a1.addSong(Collections.singletonList(s1));
            a2.addSong(Collections.singletonList(s1));
            Song s2=new Song("Goodbye",Category.Blues,Collections.singletonList(a1));
            a1.addSong(Collections.singletonList(s2));
            Listener l1=new Listener("Konrad",Collections.singletonList(s1),Arrays.asList(s1,s2));
            Listener l2=new Listener("Jan",new ArrayList<>(),Collections.singletonList(s1));
            songService.createOrUpdate(s1);
            songService.createOrUpdate(s2);
            songService.closeSession();
            listenerService.createOrUpdate(l1);
            listenerService.createOrUpdate(l2);

            listenerService.getRecommendationsByCategory(l1.getName()).forEach(System.out::println);
            listenerService.getRecommendationsByArtist(l1.getName()).forEach(System.out::println);
            listenerService.getRecommendationsBySimilarListeners(l1.getName()).forEach(System.out::println);
            listenerService.getRecommendationsByCategory(l2.getName()).forEach(System.out::println);
            listenerService.getRecommendationsByArtist(l2.getName()).forEach(System.out::println);
            listenerService.getRecommendationsBySimilarListeners(l2.getName()).forEach(System.out::println);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(QUIT_CMD))
        {
            System.exit(0);
        }
        else throw new IllegalArgumentException("No such command");
    }

    public List<String> getPossibleCommands()
    {
        return new ArrayList<String>(Arrays.asList(ADD_ARTIST_CMD_WITH_OPTIONS,
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

    //@TODO method to inject the data from JSON to database
    public void getDataFromJson(final String path)
    {

    }
}
