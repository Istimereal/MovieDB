package app.daos;

import app.entities.Actor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ActorDAO implements IDAO<Actor, Integer> {

    private final EntityManagerFactory emf;

    public ActorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Actor create(Actor actor) {
        try(EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            em.persist(actor);
            em.getTransaction().commit();
        }
        return null;
    }

    public List<Actor> getAll() {

        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Actor> query =
                    em.createQuery("SELECT a FROM Actor a", Actor.class);
            return query.getResultList();
        }
    }

    public Actor getById(Integer id){
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Actor.class, id);
        }
    }

    public Actor update(Actor actor){

        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Actor updateActor = em.merge(actor);
            em.getTransaction().commit();
            return updateActor;
        }
    }

    public boolean delete(Integer id){
        try(EntityManager em = emf.createEntityManager()) {

            Actor deleteActor = em.find(Actor.class, id);
            if(deleteActor != null){
                em.getTransaction().begin();
                em.remove(deleteActor);
                em.getTransaction().commit();
                return true;
            }
            else{return false;}
        }
    }
}


