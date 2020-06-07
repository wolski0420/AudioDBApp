package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Song;

import java.util.*;

public class ArtistService extends GenericService<Artist>{

    @Override
    Class<Artist> getEntityType() {
        return Artist.class;
    }

    // it should work with no songs given also (empty collection will be given to the constructor)
    public Artist addNewArtist(String artistName, String[] songsNames){
        final Collection<Song> songs = new ArrayList<>();
        final Collection<String> notCreatedSongs = new ArrayList<>();

        // checks artist existence (maybe there's other way)
        final Map<String,Object> artistParams = new HashMap<>();
        artistParams.put("artist_name",artistName);
        final String artistQuery = "MATCH (n:Artist{name:$artist_name}) RETURN n";
        if(executor.query(artistQuery,artistParams).hasNext()){
            System.out.println("This artist is already in database!");
            return null;
        }

        // finding songs in DB
        for(String name: songsNames){
            Map<String,Object> params = new HashMap<>();
            params.put("song_name",name);
            String songQuery = "MATCH (n:Song{name:$song_name}) RETURN n";
            Collection<Song> foundSongs = resultToSongCollection(executor.query(songQuery,params));

            if(!foundSongs.isEmpty()){
                songs.add(foundSongs.iterator().next());
            }
            else{
                notCreatedSongs.add(name);
            }
        }

        // creating in DB
        final Artist artist = new Artist(artistName,songs);
        createOrUpdate(artist);

        // creating not existing songs
        for(String name: notCreatedSongs){
            SongService songService = new SongService();
            songService.addNewSong(name,null,new String[]{artistName});
            songService.closeSession();
        }

        return artist;
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
