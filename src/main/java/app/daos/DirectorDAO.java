package app.daos;

import app.entities.Director;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DirectorDAO implements IDAO<Director, Integer> {

    private final EntityManagerFactory emf;

    public DirectorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Director create(Director director) {
        try(EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            em.persist(director);
            em.getTransaction().commit();
        }
        return null;
    }

    public List<Director> getAll() {

        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Director> query =
                    em.createQuery("SELECT g FROM Director g", Director.class);
            return query.getResultList();
        }
    }

    public Director getById(Integer id){
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Director.class, id);
        }
    }

    public Director update(Director director){

        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Director updateDirector = em.merge(director);
            em.getTransaction().commit();
            return updateDirector;
        }
    }

    public boolean delete(Integer id){
        try(EntityManager em = emf.createEntityManager()) {

            Director deleteDirector = em.find(Director.class, id);
            if(deleteDirector != null){
                em.getTransaction().begin();
                em.remove(deleteDirector);
                em.getTransaction().commit();
                return true;
            }
            else{return false;}
        }
    }
}
