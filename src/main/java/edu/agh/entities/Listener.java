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

    public Set<Category> getLikedCategory()
    {
        EnumSet<Category> categories = EnumSet.noneOf(Category.class);

        for(Song s:likedSongs)
        {
            categories = EnumSet.of(s.getCategory());
        }

        return categories;
    }

    public Set<Artist> getLikedArtist()
    {
        Set<Artist> artists = new HashSet<>();

        for(Song s:likedSongs)
        {
            artists.addAll(s.artists);
        }

        return artists;
    }

    public Collection<Song> getViewedSongs() {
        return viewedSongs;
    }

    private int getRandomNumber(int upperBoundExclusive)
    {
        Random r=new Random();
        return r.nextInt(upperBoundExclusive);
    }
}
