package edu.agh.entities;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

public abstract class Entity {
    @Id
    @GeneratedValue
    public Long id;
}
