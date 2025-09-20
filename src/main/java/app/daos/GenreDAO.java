package app.daos;
import app.entities.Genre;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class GenreDAO implements IDAO<Genre, Integer> {

    private final EntityManagerFactory emf;

    public GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Genre create(Genre genre) {
        try(EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();
        }
        return null;
    }

    public List<Genre> getAll() {

        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Genre> query =
                    em.createQuery("SELECT g FROM Genre g", Genre.class);
            return query.getResultList();
        }
    }

    public Genre getById(Integer id){
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Genre.class, id);
        }
    }

    public Genre update(Genre genre){

        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Genre updateStudent = em.merge(genre);
            em.getTransaction().commit();
            return updateStudent;
        }
    }

    public boolean delete(Integer id){
        try(EntityManager em = emf.createEntityManager()) {

            Genre deleteGenre = em.find(Genre.class, id);
            if(deleteGenre != null){
                em.getTransaction().begin();
                em.remove(deleteGenre);
                em.getTransaction().commit();
                return true;
            }
            else{return false;}
        }
    }
}
