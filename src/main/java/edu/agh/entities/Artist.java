package edu.agh.entities;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@NodeEntity
public class Artist extends Entity {
    String name;
    @Relationship(type="PERFORMED_BY",direction = Relationship.INCOMING)
    Collection<Song> songs=new ArrayList<>();

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    public Artist(String name,Collection<Song> songs)
    {
        this(name);
        this.songs=songs;
    }

    public void addSong(Collection<Song> songs)
    {
        this.songs.addAll(songs);
    }
}
