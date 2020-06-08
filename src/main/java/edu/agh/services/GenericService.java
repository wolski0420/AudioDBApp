package edu.agh.services;

import edu.agh.Neo4jSessionFactory;
import edu.agh.entities.Entity;
import org.neo4j.ogm.session.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public abstract class GenericService<T extends Entity> implements Service<T> {

    private static final int DEPTH_LIST=0;
    private static final int DEPTH_ENTITY=2;
    protected Session session= Neo4jSessionFactory.getInstance().openNeo4jSession();
    protected Neo4jSessionFactory sessionFactory=Neo4jSessionFactory.getInstance();
    protected CypherExecutor executor=new BoltCypherExecutor(session);
    @Override
    public T find(Long id) {

        return session.load(getEntityType(),id,DEPTH_ENTITY);
    }

    protected Collection<Long> getIdsFromMap(Iterator<Map<String,Object>> iterator)
    {
        Collection<Long> ids=new ArrayList<>();
        while(iterator.hasNext())
        {
            iterator.next().forEach((string,object)-> ids.add((Long)object));
        }
        return ids;
    }
    @Override
    public Collection<T> getEntitiesById(Collection<Long> ids)
    {
        Collection<T> entities=new ArrayList<>();
        ids.forEach(id->entities.add(find(id)));
        return entities;
    }

    @Override
    public void delete(Long id) {
        session.delete(session.load(getEntityType(),id));
    }

    @Override
    public T createOrUpdate(T entity) {
        session.save(entity,DEPTH_ENTITY);
        return find(entity.id);
    }
    public void closeSession()
    {
        sessionFactory.closeSession();
    }
    abstract Class<T> getEntityType();
}
