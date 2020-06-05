package edu.agh.entities;

import lombok.Getter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

@NodeEntity
public class Listener extends Entity {
    @Getter
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

    public Category getLikedOrSuggestedCategory()
    {
        final int index=getRandomNumber(likedSongs.size());
        int i=0;
        for(Song s:likedSongs)
        {
            if(i==index)
            {
                System.out.println(s.category);
                return s.getCategory();
            }
            i++;

        }
        return Category.Pop;
    }

    public Artist getLikedArtist()
    {
        final int index=getRandomNumber(likedSongs.size());
        int i=0;
        for(Song s:likedSongs)
        {
            if(i==index)
            {
                return s.getRandomArtist();
            }
            i++;
        }
        return null;
    }

    private int getRandomNumber(int upperBoundExclusive)
    {
        Random r=new Random();
        return r.nextInt(upperBoundExclusive);
    }
}
