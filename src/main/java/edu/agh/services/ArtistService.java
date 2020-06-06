package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Song;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;

import java.util.*;

public class ArtistService extends GenericService<Artist>{

    @Override
    Class<Artist> getEntityType() {
        return Artist.class;
    }

    // it should work with no songs given also (empty collection will be given to the constructor)
    public Collection<String> addNewArtist(String artistName, String[] songsNames){
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
        Artist artist = new Artist(artistName,songs);
        createOrUpdate(artist);

//        artist = createOrUpdate(artist);
//        if(artist == null) return null;

        // creating not existing songs
//        if(!notCreatedSongs.isEmpty()){
//            for(String name: notCreatedSongs){
//                SongService service = new SongService();
//                Song song = service.addNewSong(name,null,new String[]{artistName});
//                artist.addSong(Collections.singletonList(song));
//                service.closeSession();
//            }
//        }

        closeSession();
        return notCreatedSongs;
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
