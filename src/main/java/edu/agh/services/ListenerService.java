package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;

import java.util.*;

public class ListenerService extends GenericService<Listener> {

    private static final int SONGS_TO_FETCH = 10;

    @Override
    Class<Listener> getEntityType() {
        return Listener.class;
    }

    public void addNewListener(String name) {
        Listener listener = new Listener(name);
        createOrUpdate(listener);
    }

    public Collection<Song> getRecommendationsByCategory(final String name) {
        final Listener listener = findListener(name);
        if(listener == null){
            System.out.println("Listener doesn't exist in database!");
            return Collections.emptyList();
        }

        final Category category=listener.getRandomLikedCategory();

        // @TODO something is wrong here

        final Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("listener_name", listener.getName());
        params.put("num_of_songs", SONGS_TO_FETCH);
        final String query="MATCH (all_from_cat:Song{category: $category}) MATCH (all_viewed_from_cat:Song{category: $category})<-[VIEWED]-(:Listener{name: $listener_name})" +
                "WHERE (all_from_cat<>all_viewed_from_cat) RETURN DISTINCT all_from_cat LIMIT $num_of_songs";

        return resultToSongCollection(executor.query(query,params));
    }

    public Collection<Song> getRecommendationsBySimilarListeners(final String name) {
        final Listener listener = findListener(name);
        if(listener == null){
            System.out.println("Listener doesn't exist in database!");
            return Collections.emptyList();
        }

        final Map<String, Object> params = new HashMap<>();
        params.put("listener_name", listener.getName());
        params.put("num_of_songs", SONGS_TO_FETCH);
        final String query = "MATCH (list:Listener {name: $listener_name})-->(song:Song)<--(anonther_list:Listener) " +
                "WHERE (list<>anonther_list) AND (anonther_list<>list) MATCH (anonther_list)-->(anonther_list_song:Song) " +
                "WHERE (anonther_list_song<>song) RETURN anonther_list_song LIMIT $num_of_songs";


        return resultToSongCollection(executor.query(query, params));
    }

    public Collection<Song> getRecommendationsByArtist(final String name) {
        final Listener listener = findListener(name);
        if(listener == null){
            System.out.println("Listener doesn't exist in database!");
            return Collections.emptyList();
        }

        final Artist likedArtist = listener.getRandomLikedArtist();

        // @TODO something is wrong here

        final HashMap<String,Object> params=new HashMap<>();
        if(likedArtist != null) params.put("artist_name",likedArtist.getName());    // null check
        else params.put("artist_name",null);
        params.put("listener_name",listener.getName());
        params.put("num_to_fetch",SONGS_TO_FETCH);
        final String query="MATCH (song:Song)--(artists:Artist{name: $artist_name}) " +
                "MATCH (known:Song)--(:Listener{name: $listener_name}) " +
                "WHERE (song<>known) RETURN DISTINCT song LIMIT $num_to_fetch";

        return resultToSongCollection(executor.query(query,params));
    }

    public void likeSong(final String listenerName, final String title){
        final Listener listener = findListener(listenerName);
        if(listener == null){
            System.out.println("Listener doesn't exist in database!");
            return;
        }

        final Song song = findSong(title);
        if(song == null){
            System.out.println("Song doesn't exist in database!");
            return;
        }

        listener.addLikedSongs(Collections.singletonList(song));
        createOrUpdate(listener);
    }

    public void viewedSong(final String listenerName, final String title){
        final Listener listener = findListener(listenerName);
        if(listener == null){
            System.out.println("Listener doesn't exist in database!");
            return;
        }

        final Song song = findSong(title);
        if(song == null){
            System.out.println("Song doesn't exist in database!");
            return;
        }

        listener.addViewedSongs(Collections.singletonList(song));
        createOrUpdate(listener);
    }

    private Collection<Song> resultToSongCollection(Iterator<Map<String, Object>> iterator) {
        final Collection<Song> songs = new ArrayList<>();

        while (iterator.hasNext()) {
            Map<String, Object> entry = iterator.next();
            entry.forEach((string, object) -> songs.add((Song) object));
        }

        return songs;
    }

    private Collection<Listener> resultToListenerCollection(Iterator<Map<String, Object>> iterator){
        final Collection<Listener> listeners = new ArrayList<>();

        while(iterator.hasNext()){
            Map<String,Object> entry = iterator.next();
            entry.forEach((string,object)->listeners.add((Listener) object));
        }

        return listeners;
    }

    private Listener findListener(final String name){
        final HashMap<String,Object> listenerParams = new HashMap<>();
        listenerParams.put("listener_name",name);
        final String listenerQuery = "MATCH (n:Listener{name:$listener_name}) RETURN n LIMIT 1";
        final Collection<Listener> listeners = resultToListenerCollection(executor.query(listenerQuery,listenerParams));

        return listeners.iterator().next();
    }

    private Song findSong(final String name){
        final HashMap<String,Object> songParams = new HashMap<>();
        songParams.put("title",name);
        final String songQuery = "MATCH (n:Song{name:$title}) RETURN n LIMIT 1";
        final Collection<Song> songs = resultToSongCollection(executor.query(songQuery,songParams));

        return songs.iterator().next();
    }
}
