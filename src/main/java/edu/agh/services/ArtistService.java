package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Song;

import java.util.*;

public class ArtistService extends GenericService<Artist>{

    @Override
    Class<Artist> getEntityType() {
        return Artist.class;
    }

    public Artist addNewArtist(String artistName){
        // checks artist existence
        final Map<String,Object> artistParams = new HashMap<>();
        artistParams.put("artist_name",artistName);
        final String artistQuery = "MATCH (n:Artist{name:$artist_name}) RETURN DISTINCT n";
        if(executor.query(artistQuery,artistParams).hasNext()) {            // it's faster, we avoid two O(n) methods
            System.out.println("This artist is already in database!");
            return null;
        }

        // creating artist and saving in DB
        System.out.println("Added new artist named " + artistName);
        return createOrUpdate(new Artist(artistName));
    }
}
