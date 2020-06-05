package edu.agh.services;

import edu.agh.entities.Category;
import edu.agh.entities.Listener;
import edu.agh.entities.Song;
import org.neo4j.ogm.model.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ListenerService extends GenericService<Listener>{
    @Override
    Class<Listener> getEntityType() {
        return Listener.class;
    }

    //recommendation for particular listener based on his previous liked categories
    Collection<Song> getRecommendationByCategory(final Listener listener)
    {
        //tutaj coś ala:
        //    MATCH (greet:Greeting) where greet.message='hello, world'
//        CREATE (product:Product{title:'elo'}) MERGE (product)-[IS:IN]->(greet)
//KATEGORIE DO PIOSENEK POD WARUNKIEM ŻE SIĘ NAJPIERW STWORZY KATEGORIE -- TRZEBA SPRAWDZIĆ WCZEŚNIEJ CZY KATEGORIA ISTNIEJE
//MATCH (category:Category) where category.name='1'
//CREATE (song:Song{title:'2', year:'3'}) MERGE (song)->[IS_OF]->(category)
        final Category likedCategory=listener.getLikedCategory();
        final Map<String,Object> params=new HashMap<>();
        params.put("category",likedCategory);
        final String query="MATCH (n:Song) WHERE n.category=$category RETURN n";
        Result result=session.query(query,params);
        return resultToSongCollection(result);

    }

    //@TODO
    private Collection<Song> resultToSongCollection(Result result)
    {
        Collection<Song> songs=new ArrayList<>();
        result.forEach();

    }
}
