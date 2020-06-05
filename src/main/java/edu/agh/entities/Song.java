package edu.agh.entities;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NodeEntity
public class Song extends Entity {
    String name;
    @Setter @Getter
    Category category;
    @Relationship(type="PERFORMED_BY")
    Collection<Artist> artists=new ArrayList<>();


    public Song() {
    }

    public Song(String name) {
        this.name = name;
    }

    public Song(String name,Collection<Artist> artists)
    {
        this(name);
        this.artists=artists;
    }
    public Song(String name,Category category,Collection<Artist> artists)
    {
        this(name,artists);
        this.category=category;
    }
    public void addArtists(final List<Artist> artists)
    {
        this.artists.addAll(artists);
    }
}
