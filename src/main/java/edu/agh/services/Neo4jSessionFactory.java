package edu.agh.services;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.logging.Level;

public class Neo4jSessionFactory {
    private static final Configuration config = new Configuration.Builder()
            .uri("bolt://localhost")
            .credentials("neo4j", "password")
            .build();
    private static final SessionFactory sf=new SessionFactory(config,"edu.agh.entities");
    private static Neo4jSessionFactory instance=null;

    private Neo4jSessionFactory()
    {
        java.util.logging.Logger.getLogger("org.neo4j.ogm.drivers.bolt.request.BoltRequest").setLevel(Level.WARNING);
    }
    public static Neo4jSessionFactory getInstance()
    {
        if(instance==null){
            instance=new Neo4jSessionFactory();
        }
        return instance;
    }

    public Session openNeo4jSession()
    {
        return sf.openSession();
    }

    public void closeSession()
    {
        sf.close();
    }
}
