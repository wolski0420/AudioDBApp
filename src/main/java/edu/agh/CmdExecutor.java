package edu.agh;

import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;
import edu.agh.services.ArtistService;
import edu.agh.services.ListenerService;
import edu.agh.services.SongService;

import java.util.*;

public class CmdExecutor {
    private static final String ADD_SONG_CMD="add_song";
    private static final String ADD_ARTIST_CMD="add_artist";
    private static final String ADD_LISTENER_CMD="add_listener";
    private static final String GET_RECOMMENDATION_BY_CATEGORY_CMD="get_recommendation_by_category";
    private static final String GET_RECOMMENDATION_BY_ARTIST_CMD="get_recommendation_by_artist";
    private static final String GET_RECOMMENDATION_BY_LISTENER_CMD="get_recommendation_by_listener";
    private final SongService songService=new SongService();
    private final ListenerService listenerService=new ListenerService();
    private final ArtistService artistService = new ArtistService();

    //the command should look like, eg: add_song title category artist artist artist
    void execute(String cmd)
    {
        final String[] words=cmd.split("\\s+");

        if(words[0].equalsIgnoreCase(ADD_SONG_CMD))
        {
            SongService service = new SongService();
            Collection<String> notCreatedArtists = service.addNewSong(words[1],words[2],Arrays.copyOfRange(words,3,words.length));
            service.closeSession();
            //System.out.println(notCreatedArtists);

            for(String artistName: notCreatedArtists){
                ArtistService service2 = new ArtistService();
                Collection<String> notCreatedSongs = service2.addNewArtist(artistName,new String[]{words[1]});
                service2.closeSession();
                //System.out.println(notCreatedSongs);
            }



            Artist artist = new Artist("artist");
            List<Song> liked=new ArrayList<>();
            Song s=new Song("title",Collections.singletonList(artist));
            s.setCategory(Category.Pop);
            liked.add(s);
            Listener l1=new Listener("Konrad",liked,liked);
            //System.out.println(listenerService.getRecommendationsByArtist(l1));
            System.out.println(listenerService.getRecommendationsByCategory(l1));

            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(ADD_ARTIST_CMD))
        {
            artistService.addNewArtist(words[1],Arrays.copyOfRange(words,2,words.length));
            artistService.closeSession();
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
            listenerService.getRecommendationsByCategory(l1).forEach(System.out::println);
            listenerService.closeSession();//!!! this line is necessary in every command
            //delete end
        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_ARTIST_CMD))
        {
            List<Song> liked = new ArrayList<>();
            Song s = new Song("Hi");
            List<Artist> artists = new ArrayList<>();
            artists.add(new Artist("Krawczyk"));
            s.addArtists(artists);
            liked.add(s);
            Listener l1 = new Listener("Lukasz",liked,liked);
            System.out.println(listenerService.getRecommendationsByArtist(l1));
            listenerService.closeSession();
        }
        else if(words[0].equalsIgnoreCase(GET_RECOMMENDATION_BY_LISTENER_CMD)){

            //@TODO for fast test

            Listener l1 = new Listener("Jan");
            listenerService.getRecommendationsBySimilarListeners(l1).forEach(System.out::println);
            listenerService.closeSession();
        }
        else throw new IllegalArgumentException("No such command");

        //@TODO any other options? discuss needed
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
