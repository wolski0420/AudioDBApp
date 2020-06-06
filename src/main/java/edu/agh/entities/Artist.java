package edu.agh.entities;

import lombok.Getter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@NodeEntity
public class Artist extends Entity {
    @Getter
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

    // not sure what is better: id/name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;
        Artist artist = (Artist) o;
        return this.name.equals(artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
