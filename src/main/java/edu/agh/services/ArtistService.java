package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Song;

import java.util.*;

public class ArtistService extends GenericService<Artist>{

    @Override
    Class<Artist> getEntityType() {
        return Artist.class;
    }

    public Artist addNewArtist(final String artistName){
        // checks artist existence
        final Map<String,Object> artistParams = new HashMap<>();
        artistParams.put("artist_name",artistName);
        final String artistQuery = "MATCH (n:Artist{name:$artist_name}) RETURN DISTINCT n";
        if(executor.query(artistQuery,artistParams).hasNext()) {            // it's faster, we avoid two O(n) methods
            System.out.println("This artist is already in database!");
            return null;
        }

        // creating artist and saving in DB
        System.out.println(String.format("Added new artist named \"%s\"",artistName));
        return createOrUpdate(new Artist(artistName));
    }

    public Collection<Song> findSongsByArtistName(final String artistName){
        final Artist artist = findArtistByName(artistName);
        if(artist == null){
            System.out.println("This artist doesn't exist in database!");
            return Collections.emptyList();
        }

        System.out.println(String.format("Getting songs of artist named \"%s\"",artistName));
        return artist.getSongs();
    }

    private Artist findArtistByName(final String name){
        final HashMap<String,Object> artistParams = new HashMap<>();
        artistParams.put("artist_name",name);
        final String artistQuery = "MATCH (n:Artist{name:$artist_name}) RETURN id(n) LIMIT 1";
        Iterator<Map<String,Object>> itr = executor.query(artistQuery,artistParams);

        if(!itr.hasNext()) return null;

        final Long id = (Long)itr.next().get("id(n)");

        return find(id);
    }
}
