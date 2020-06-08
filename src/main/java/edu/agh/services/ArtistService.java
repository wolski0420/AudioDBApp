package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Song;

import java.util.*;

public class ArtistService extends GenericService<Artist>{

    @Override
    Class<Artist> getEntityType() {
        return Artist.class;
    }

    //@TODO correction needed- look getSongsForListenerFromArtist in ListenerService
    // it should work with no songs given also (empty collection will be given to the constructor)
    public Artist addNewArtist(String artistName, String[] songsNames){
        final Collection<Song> songs = new ArrayList<>();
        final Collection<String> notCreatedSongs = new ArrayList<>();

        // checks artist existence (maybe there's other way)
        final Map<String,Object> artistParams = new HashMap<>();
        artistParams.put("artist_name",artistName);
        final String artistQuery = "MATCH (n:Artist{name:$artist_name}) RETURN DISTINCT id(n)";
        Collection<Long> ids=getIdsFromMap(executor.query(artistQuery,artistParams)) ;
        Collection<Artist> arristsFromDb=this.getEntitiesById(ids);
        arristsFromDb.forEach(artist->artist.addSong());

        //@TODO change null
        return null;
    }

    private Collection<Song> resultToSongCollection(Iterator<Map<String,Object>> iterator){
        final Collection<Song> songs = new ArrayList<>();

        while(iterator.hasNext()){
            Map<String,Object> entry = iterator.next();
            entry.forEach((string,object)->songs.add((Song) object));
        }

        return songs;
    }
}
