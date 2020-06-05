package edu.agh.services;

import edu.agh.Neo4jSessionFactory;
import edu.agh.entities.Entity;
import org.neo4j.ogm.session.Session;

public abstract class GenericService<T extends Entity> implements Service<T> {

    private static final int DEPTH_LIST=0;
    private static final int DEPTH_ENTITY=1;
    protected Session session= Neo4jSessionFactory.getInstance().openNeo4jSession();
    protected Neo4jSessionFactory sessionFactory=Neo4jSessionFactory.getInstance();
    protected CypherExecutor executor=new BoltCypherExecutor(session);
    @Override
    public T find(Long id) {

        return session.load(getEntityType(),id,DEPTH_ENTITY);
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
