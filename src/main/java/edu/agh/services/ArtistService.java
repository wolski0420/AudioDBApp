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

        ArtistService service = new ArtistService();
        Artist artist = new Artist(artistName,songs);
        service.createOrUpdate(artist);
        service.closeSession();

        // creating not existing songs
        if(!notCreatedSongs.isEmpty()){
            for(String name: notCreatedSongs){
                SongService songService = new SongService();
                Song song = songService.addNewSong(name,null,new String[]{artistName});
                artist.addSong(Collections.singletonList(song));
                songService.closeSession();
            }

            ArtistService service2 = new ArtistService();
            service2.createOrUpdate(artist);
            service2.closeSession();
        }

        closeSession();
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
