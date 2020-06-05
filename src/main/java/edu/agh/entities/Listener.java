package edu.agh.entities;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@NodeEntity
public class Listener extends Entity {
    String name;
    @Relationship(type="VIEWED")
    Collection<Song> viewedSongs=new ArrayList<>();
    @Relationship(type="LIKED")
    Collection<Song> likedSongs=new ArrayList<>();

    public Listener(String name) {
        this.name = name;
    }

    public Listener(String name, Collection<Song> viewedSongs, Collection<Song> likedSongs) {
        this.name = name;
        this.viewedSongs = viewedSongs;
        this.likedSongs = likedSongs;
    }

    public void addLikedSongs(Collection<Song> likedSongs)
    {
        this.likedSongs.addAll(likedSongs);
    }

    public void addViewedSongs(Collection<Song> viewedSongs)
    {
        this.likedSongs.addAll(viewedSongs);
    }
}
