package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Song;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;

import java.text.CollationElementIterator;
import java.util.*;

public class SongService extends GenericService<Song>  {

    @Override
    Class<Song> getEntityType() {
        return Song.class;
    }

    // it should work with no artists names given also (empty collection will be given to song constructor)
    public Collection<String> addNewSong(final String title, final String categoryName, final String[] artistsNames){
        final Collection<Artist> artists = new ArrayList<>();
        final Collection<String> notCreatedArtists = new ArrayList<>();
        String query = "MATCH (n) DETACH DELETE n";
        //executor.query(query,Collections.emptyMap());

        // finding artists in DB
        for(String name : artistsNames){

            Map<String,Object> params = new HashMap<>();
            params.put("artist_name",name);
            String artistQuery = "MATCH (n:Artist{name:$artist_name}) RETURN n";
            Collection<Artist> foundArtists = resultToArtistCollection(executor.query(artistQuery,params));

            if(!foundArtists.isEmpty()){
                Artist artist = foundArtists.iterator().next();
                artists.add(artist);
            }
            else{
                notCreatedArtists.add(name);
            }
        }

        Category category = Category.fromString(categoryName);   // null when invalid category given (check impl.)

        // creating in DB
        Song song;
        if(category == null) song = new Song(title,artists);
        else song = new Song(title,category,artists);
        createOrUpdate(song);

        // creating not existing artists
//        if(!notCreatedArtists.isEmpty()){
//            for(String name: notCreatedArtists){
//                ArtistService service = new ArtistService();
//                Artist artist = service.addNewArtist(name,new String[]{title});
//                song.addArtists(Collections.singletonList(artist));
//                service.closeSession();
//            }
//        }

        closeSession();
        return notCreatedArtists;
    }

    private Collection<Artist> resultToArtistCollection(Iterator<Map<String,Object>> iterator){
        final Collection<Artist> artists = new ArrayList<>();

        while(iterator.hasNext()){
            Map<String,Object> entry = iterator.next();
            entry.forEach((string,object)->artists.add((Artist)object));
        }

        return artists;
    }
}
