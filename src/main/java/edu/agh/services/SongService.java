package edu.agh.services;

import edu.agh.entities.Artist;
import edu.agh.entities.Category;
import edu.agh.entities.Song;

import java.util.*;

public class SongService extends GenericService<Song>  {

    @Override
    Class<Song> getEntityType() {
        return Song.class;
    }

    // it should work with no artists names given also (empty collection will be given to song constructor)
    public Song addNewSong(final String title, final String categoryName, final String[] artistsNames){
        final Collection<Artist> artists = new ArrayList<>();
        final Collection<String> notCreatedArtists = new ArrayList<>();

        // checks song existence (maybe there's other way)
        final Map<String,Object> songParams = new HashMap<>();
        songParams.put("title",title);
        final String songQuery = "MATCH (n:Song{name:$title}) RETURN n";
        if(executor.query(songQuery,songParams).hasNext()) {
            System.out.println("This song is already in database!");
            return null;
        }

        // finding artists in DB
        for(String name : artistsNames){
            Map<String,Object> artistParams = new HashMap<>();
            artistParams.put("artist_name",name);
            String artistQuery = "MATCH (n:Artist{name:$artist_name}) RETURN n";
            Collection<Artist> foundArtists = resultToArtistCollection(executor.query(artistQuery,artistParams));

            if(!foundArtists.isEmpty()){
                artists.add(foundArtists.iterator().next());
            }
            else{
                notCreatedArtists.add(name);
            }
        }

        final Category category = Category.fromString(categoryName);   // null when invalid category given (check impl.)

        // creating in DB
        final Song song;
        if(category == null) song = new Song(title,artists);
        else song = new Song(title,category,artists);
        createOrUpdate(song);

        // creating not existing artists
        for(String name: notCreatedArtists){
            ArtistService artistService = new ArtistService();
            artistService.addNewArtist(name,new String[]{title});
            artistService.closeSession();
        }

        return song;
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
