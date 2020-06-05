package edu.agh.services;

import edu.agh.entities.Song;

public class SongService extends GenericService<Song>  {

    @Override
    Class<Song> getEntityType() {
        return Song.class;
    }
}
