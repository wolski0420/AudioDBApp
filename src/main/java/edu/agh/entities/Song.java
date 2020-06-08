package edu.agh.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@NodeEntity
public class Song extends Entity {
    @Getter
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
        this.artists.addAll(artists);
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

    public Collection<Artist> getArtists() {
        return artists;
    }

    public Artist getRandomArtist()
    {
        final int index=getRandomNumber(artists.size());
        int i=0;
        for(Artist a:artists)
        {
            if(i==index)
            {
                return a;
            }
            i++;
        }
        return null;
    }

    private int getRandomNumber(int upperBoundExclusive)
    {
        if(upperBoundExclusive < 1) return 0;
        return new Random().nextInt(upperBoundExclusive);
    }

    @Override
    public String toString()
    {
        StringBuilder artistsString=new StringBuilder();
        artists.forEach(artist->artistsString.append(String.format("%s, ",artist.toString())));
        return String.format("%s of %s PERFORMED BY %s",this.name,Category.toString(this.category), artistsString.toString());
    }
}
