package edu.agh.services;

import org.neo4j.ogm.session.Session;

import java.util.Iterator;
import java.util.Map;

public class BoltCypherExecutor implements CypherExecutor {
    private final Session session;
    public BoltCypherExecutor(Session session)
    {
        this.session=session;
    }

    @Override
    public Iterator<Map<String, Object>> query(String statement, Map<String, Object> parameters) {
        try
        {
            return session.query(statement,parameters).iterator();
        }
        catch(Exception e)
        {
            System.out.println("Error querying DB");
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
