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

    public void addNewListener(String name){
        Listener listener = new Listener(name);
        createOrUpdate(listener);
        closeSession();
    }

    // now it returns list of missed songs from liked categories
    public Collection<Song> getRecommendationsByCategory(final Listener listener)
    {
        final Set<Category> likedCategories = listener.getLikedCategories();
        final Collection<Song> songs = new ArrayList<>();
        final Collection<String> viewedSongs = new ArrayList<>();

        for(Song song: listener.getViewedSongs()){
            viewedSongs.add(song.getName());
        }

        for(Category category: likedCategories){
            Map<String,Object> params = new HashMap<>();
            params.put("category",category);
            params.put("viewed_songs",viewedSongs);
            String query = "MATCH (s:Song{category:$category}) " +
                    "WHERE NONE(song_name IN $viewed_songs WHERE s.name=song_name) " +
                    "RETURN s";
            songs.addAll(resultToSongCollection(executor.query(query,params)));
        }

        songs.removeAll(listener.getViewedSongs()); // maybe it can be replaced by better query

        return songs;
    }

    public Collection<Song> getRecommendationsBySimilarListeners(Listener listener)
    {
        final Map<String,Object> params = new HashMap<>();
        params.put("listener_name",listener.getName());

        final String query="MATCH (:Listener {name:$listener_name})->(song:Song)<-(anonther_list:Listener) " +
                "MATCH (anonther_list)->(anonther_list_song:Song) " +
                "WHERE (anonther_list_song<>song) RETURN anonther_list_song";

        return resultToSongCollection(executor.query(query,params));
    }

    // now it returns list of missed songs from liked artists
    public Collection<Song> getRecommendationsByArtist(Listener listener)
    {
        final Set<Artist> likedArtists = listener.getLikedArtists();
        final Collection<Song> songs = new ArrayList<>();
        final Collection<String> viewedSongs = new ArrayList<>();

        for(Song song: listener.getViewedSongs()){
            viewedSongs.add(song.getName());
        }

        for(Artist artist: likedArtists){
            Map<String,Object> params=new HashMap<>();
            params.put("liked_artist",artist.getName());
            params.put("viewed_songs",viewedSongs);
            String query="MATCH (s:Song)-[:PERFORMED_BY]-(a:Artist) " +
                    "WHERE a.name=$liked_artist " +
                    "AND NONE(song_name IN $viewed_songs WHERE s.name=song_name) " +
                    "RETURN s";
            songs.addAll(resultToSongCollection(executor.query(query,params)));
        }

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
