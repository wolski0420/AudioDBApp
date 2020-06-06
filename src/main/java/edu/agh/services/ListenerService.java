package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;

import java.util.*;

public class ListenerService extends GenericService<Listener>{

    @Override
    Class<Listener> getEntityType() {
        return Listener.class;
    }

//    public Listener addNewListener()

    // now it returns list of missed songs from liked categories
    public Collection<Song> getRecommendationsByCategory(final Listener listener)
    {
        final Set<Category> likedCategories = listener.getLikedCategory();
        final Collection<Song> songs = new ArrayList<>();

        for(Category category: likedCategories){
            Map<String,Object> params = new HashMap<>();
            params.put("category",category);
            String query = "MATCH (n:Song{category:$category}) RETURN n";
            songs.addAll(resultToSongCollection(executor.query(query,params)));
        }

        songs.removeAll(listener.getViewedSongs()); // maybe it can be replaced by better query

        return songs;
    }


//        @TODO TEST IT SOMEHOW, but the query should be ok
    public Collection<Song> getRecommendationsBySimilarListeners(Listener listener)
    {
        final Map<String,Object> params = new HashMap<>();
        params.put("listener_name",listener.getName());

        final String query="MATCH (:Listener {name:$listener_name})->(song:Song)<-(anonther_list:Listener)" +
                "MATCH (anonther_list)->(anonther_list_song:Song) " +
                "WHERE (anonther_list_song<>song) RETURN anonther_list_song";

        return resultToSongCollection(executor.query(query,params));
    }

    // now it returns list of missed songs from liked artists
    public Collection<Song> getRecommendationsByArtist(Listener listener)
    {
        final Set<Artist> likedArtists = listener.getLikedArtist();
        final Collection<Song> songs = new ArrayList<>();

        for(Artist artist: likedArtists){
            Map<String,Object> params=new HashMap<>();
            params.put("liked_artist",artist.getName());
            String query="MATCH (n:Song)" +
                    "WHERE ANY(a IN n.artists WHERE a = $liked_artist)" +
                    "RETURN n";
            songs.addAll(resultToSongCollection(executor.query(query,params)));
        }

        songs.removeAll(listener.getViewedSongs()); // maybe it can be replaced by better query

        return songs;
    }


    private Collection<Song> resultToSongCollection(Iterator<Map<String,Object>> iterator)
    {
        final Collection<Song> songs=new ArrayList<>();

        while (iterator.hasNext())
        {
            Map<String,Object> entry=iterator.next();
            entry.forEach((string,object)->songs.add((Song)object));
        }

        return songs;
    }
}
