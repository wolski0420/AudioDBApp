package edu.agh.entities;

import lombok.Getter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

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

    public Listener() {
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
        this.viewedSongs.addAll(viewedSongs);
    }

    public Category getRandomLikedCategory()
    {
        final int idx=getRandomNumber(likedSongs.size());
        int i=0;
        for(Song s:likedSongs)
        {
            if(i==idx)
                return s.category;
            i++;
        }
        return null;
    }

    public Artist getRandomLikedArtist()
    {
        final int idx=getRandomNumber(likedSongs.size());
        int i=0;
        for(Song s:likedSongs)
        {
            if(i==idx)
                return s.getRandomArtist();
            i++;
        }
        return null;
    }

    public Collection<Song> getViewedSongs() {
        return viewedSongs;
    }

    private int getRandomNumber(int upperBoundExclusive)
    {
        if(upperBoundExclusive < 1) return 0;
        return new Random().nextInt(upperBoundExclusive);
    }
}
