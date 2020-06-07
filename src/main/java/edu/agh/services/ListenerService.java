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
        closeSession();
    }

    public Collection<Song> getRecommendationsByCategory(final Listener listener) {
        final Category category=listener.getRandomLikedCategory();

        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("listener_name", listener.getName());
        params.put("num_of_songs", SONGS_TO_FETCH);
        String query="MATCH (all_from_cat:Song{category: $category}) MATCH (all_viewed_from_cat:Song{category: $category})<-[VIEWED]-(:Listener{name: $listener_name})" +
                "WHERE (all_from_cat<>all_viewed_from_cat) RETURN all_from_cat LIMIT $num_of_songs";

        return resultToSongCollection(executor.query(query,params));
    }

    public Collection<Song> getRecommendationsBySimilarListeners(final Listener listener) {
        final Map<String, Object> params = new HashMap<>();
        params.put("listener_name", listener.getName());
        params.put("num_of_songs", SONGS_TO_FETCH);
        final String query = "MATCH (list:Listener {name: $listener_name})-->(song:Song)<--(anonther_list:Listener) " +
                "WHERE (list<>anonther_list) AND (anonther_list<>list) MATCH (anonther_list)-->(anonther_list_song:Song) " +
                "WHERE (anonther_list_song<>song) RETURN anonther_list_song LIMIT $num_of_songs";


        return resultToSongCollection(executor.query(query, params));
    }

    //@TODO TEST
    public Collection<Song> getRecommendationsByArtist(Listener listener) {
        final Artist likedArtist = listener.getRandomLikedArtist();
        final HashMap<String,Object> params=new HashMap<>();
        params.put("artist_name",likedArtist.getName());
        params.put("listener_name",listener.getName());
        params.put("num_to_fetch",SONGS_TO_FETCH);
        final String query="MATCH (song:Song)--(artists:Artist{name: $artist_name}) " +
                "MATCH (known:Song)--(:Listener{name: $listener_name}) " +
                "WHERE (song<>known) RETURN song LIMIT $num_to_fetch";

        return resultToSongCollection(executor.query(query,params));
    }


    private Collection<Song> resultToSongCollection(Iterator<Map<String, Object>> iterator) {
        final Collection<Song> songs = new ArrayList<>();
        while (iterator.hasNext()) {
            Map<String, Object> entry = iterator.next();
            entry.forEach((string, object) -> songs.add((Song) object));
        }

        return songs;
    }
}
