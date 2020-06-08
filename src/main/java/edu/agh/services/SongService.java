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

    public Song addNewSong(final String title, final String categoryName, final String[] artistsNames){
        // checks song existence
        final Map<String,Object> songParams = new HashMap<>();
        songParams.put("title",title);
        final String songQuery = "MATCH (n:Song{name:$title}) RETURN n";
        if(executor.query(songQuery,songParams).hasNext()) {
            System.out.println("This song is already in database!");
            return null;
        }

        final Collection<Artist> artists = new ArrayList<>();
        final Collection<String> notCreatedArtists = new ArrayList<>();

        // finding artists in DB
        for(String name : artistsNames){
            Map<String,Object> artistParams = new HashMap<>();
            artistParams.put("artist_name",name);
            String artistQuery = "MATCH (n:Artist{name:$artist_name}) RETURN DISTINCT id(n)";
            Collection<Artist> foundArtists = resultToArtistCollection(executor.query(artistQuery,artistParams));

            if(!foundArtists.isEmpty()){
                artists.add(foundArtists.iterator().next());
            }
            else{
                notCreatedArtists.add(name);
            }
        }

        // creating not existing artists
        ArtistService artistService = new ArtistService();
        notCreatedArtists.forEach((name)->artists.add(artistService.addNewArtist(name)));
        artistService.closeSession();

        // creating in DB
        System.out.println(String.format("Added new song titled \"%s\"",title));
        return createOrUpdate(new Song(title,Category.fromString(categoryName),artists));
    }

    public Collection<Artist> findArtistsByTitle(final String title){
        final Song song = findSongByName(title);
        if(song == null){
            System.out.println("This song doesn't exist in database!");
            return Collections.emptyList();
        }

        System.out.println(String.format("Getting artists of song titled \"%s\"",title));
        return song.getArtists();
    }

    public Song findSongByName(final String name){
        final HashMap<String,Object> songParams = new HashMap<>();
        songParams.put("title",name);
        final String songQuery = "MATCH (n:Song{name:$title}) RETURN id(n) LIMIT 1";
        Iterator<Map<String,Object>> itr=executor.query(songQuery,songParams);

        if(!itr.hasNext()) return null;

        final Long id=(Long)itr.next().get("id(n)");

        return find(id);
    }

    private Collection<Artist> resultToArtistCollection(Iterator<Map<String,Object>> iterator){
        final Collection<Long> artistsIDs = new ArrayList<>();

        while(iterator.hasNext()){
            Map<String,Object> entry = iterator.next();
            entry.forEach((string,object)->artistsIDs.add((Long)object));
        }

        return new ArtistService().getEntitiesById(artistsIDs);
    }
}
