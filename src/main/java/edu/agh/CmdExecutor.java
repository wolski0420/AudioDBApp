package edu.agh;

import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;
import edu.agh.services.ArtistService;
import edu.agh.services.ListenerService;
import edu.agh.services.SongService;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.*;

public class CmdExecutor {
    private static final String CLEAR="clear";
    private static final String ADD_SONG_CMD="add_song";
    private static final String ADD_ARTIST_CMD="add_artist";
    private static final String ADD_LISTENER_CMD="add_listener";
    private static final String GET_RECOMMENDATION_BY_CATEGORY_CMD="get_recommendation_by_category";
    private static final String GET_RECOMMENDATION_BY_ARTIST_CMD="get_recommendation_by_artist";
    private static final String GET_RECOMMENDATION_BY_LISTENER_CMD="get_recommendation_by_listener";
    private static final String LISTENER_LIKE_SONG="listener_like_song";
    private static final String LISTENER_VIEWED_SONG="listener_viewed_song";
    private static final String TEST="test";
    private static final String QUIT="quit";
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
                System.out.println("Bad format! Type: " + ADD_SONG_CMD + " [title] [category] [artists_names...]");
                return;
            }

            songService.addNewSong(words[1],words[2],Arrays.copyOfRange(words,3,words.length));
            songService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(ADD_ARTIST_CMD))
        {
            if(words.length < 2) {
                System.out.println("Bad format! Type: " + ADD_ARTIST_CMD + " [name] [songs_titles...]");
                return;
            }

            artistService.addNewArtist(words[1],Arrays.copyOfRange(words,2,words.length));
            artistService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(ADD_LISTENER_CMD))
        {
            if(words.length < 2) {
                System.out.println("Bad format! Type: " + ADD_LISTENER_CMD + " [name]");
                return;
            }

            listenerService.addNewListener(words[1]);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_CATEGORY_CMD))
        {
            if(words.length < 2){
                System.out.println("Bad format! Type: " + GET_RECOMMENDATION_BY_CATEGORY_CMD + " [listener_name]");
                return;
            }

            listenerService.getRecommendationsByCategory(words[1]).forEach(System.out::println);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_ARTIST_CMD))
        {
            if(words.length < 2){
                System.out.println("Bad format! Type: " + GET_RECOMMENDATION_BY_ARTIST_CMD + " [listener_name]");
                return;
            }

            listenerService.getRecommendationsByArtist(words[1]).forEach(System.out::println);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_LISTENER_CMD))
        {
            if(words.length < 2){
                System.out.println("Bad format! Type: " + GET_RECOMMENDATION_BY_LISTENER_CMD + " [listener_name]");
                return;
            }

            listenerService.getRecommendationsBySimilarListeners(words[1]).forEach(System.out::println);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(LISTENER_LIKE_SONG))
        {
            if(words.length < 3){
                System.out.println("Bad format! Type: " + LISTENER_LIKE_SONG + " [listener_name] [song_name]");
                return;
            }

            listenerService.likeSong(words[1],words[2]);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(LISTENER_VIEWED_SONG))
        {
            if(words.length < 3){
                System.out.println("Bad format! Type: " + LISTENER_VIEWED_SONG + " [listener_name] [song_name]");
                return;
            }

            listenerService.viewedSong(words[1],words[2]);
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(CLEAR)){
            Session session = Neo4jSessionFactory.getInstance().openNeo4jSession();
            session.query("MATCH (n) DETACH DELETE n",Collections.emptyMap());
            Neo4jSessionFactory.getInstance().closeSession();
        }
        else if(words[0].equalsIgnoreCase(TEST))
        {
            Artist a1=new Artist("Adele");
            Artist a2=new Artist("Madele");
            Song s1=new Song("Hello",Category.Blues,Arrays.asList(a1,a2));
            a1.addSong(Collections.singletonList(s1));
            a2.addSong(Collections.singletonList(s1));
            Song s2=new Song("Goodbye",Category.Blues,Collections.singletonList(a1));
            a1.addSong(Collections.singletonList(s2));
            Listener l1=new Listener("Konrad",Arrays.asList(s1,s2),Collections.singletonList(s1));
            Listener l2=new Listener("Jan",Collections.singletonList(s1),new ArrayList<>());
            songService.createOrUpdate(s1);
            songService.createOrUpdate(s2);
            listenerService.createOrUpdate(l1);
            listenerService.createOrUpdate(l2);
            Collection<Song> songs=listenerService.getRecommendationsByArtist(l2.getName());
            listenerService.closeSession();
            songs.forEach(song-> System.out.println(song));
        }
        else if(words[0].equalsIgnoreCase(QUIT))
        {
            System.exit(0);
        }
        else throw new IllegalArgumentException("No such command");

        //@TODO any other options? discuss needed
    }

    //@TODO enhance it with possible options
    public List<String> getPossibleCommands()
    {
        return new ArrayList<String>(Arrays.asList(ADD_ARTIST_CMD + " [name] [songs_titles...]",
                ADD_SONG_CMD + " [title] [category] [artists_names...]",
                ADD_LISTENER_CMD + " [name]",
                GET_RECOMMENDATION_BY_CATEGORY_CMD + " [listener_name]",
                GET_RECOMMENDATION_BY_ARTIST_CMD + " [listener_name]",
                GET_RECOMMENDATION_BY_LISTENER_CMD + " [listener_name]",
                LISTENER_LIKE_SONG + " [listener_name] [song_name]",
                LISTENER_VIEWED_SONG + " [listener_name] [song_name]"));
    }

    //@TODO method to inject the data from JSON to database
    public void getDataFromJson(final String path)
    {

    }
}
