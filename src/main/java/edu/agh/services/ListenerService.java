package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;
import org.neo4j.ogm.model.Result;

import java.util.*;

public class ListenerService extends GenericService<Listener>{
    @Override
    Class<Listener> getEntityType() {
        return Listener.class;
    }


    //@TODO upgrade this query, because it shows songs which have already been liked or listened to
        public Collection<Song> getRecommendationsByCategory(final Listener listener)
    {
        final Category likedCategory=listener.getLikedOrSuggestedCategory();
        final Map<String,Object> params=new HashMap<>();
        params.put("category",likedCategory);
        final String query="MATCH (n:Song{category:'$category'}) RETURN n";
        return resultToSongCollection(executor.query(query,params));
    }


//        @TODO TEST IT SOMEHOW, but the query should be ok
    public Collection<Song> getRecommendationsBySimilarListeners(Listener listener)
    {
        final Map<String,Object> params=new HashMap<>();
        params.put("listener_name",listener.getName());
        final String query="MATCH (:Listener {name:$listener_name})->(song:Song)<-(anonther_list:Listener)" +
                "MATCH (anonther_list)->(anonther_list_song:Song) " +
                "WHERE (anonther_list_song<>song) RETURN anonther_list_song";
        return resultToSongCollection(executor.query(query,params));
    }
    //        @TODO fill the query, test it maybe
    public Collection<Song> getRecommendationsByArtist(Listener listener)
    {
        final Artist artist=listener.getLikedArtist();
        final Map<String,Object> params=new HashMap<>();
        params.put("liked_artist",artist.getName());
        final String query="MATCH (:Song {})"
        return resultToSongCollection(executor.query(query,params));
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
