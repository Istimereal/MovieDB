package app.daos;

import app.entities.Movie;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

    public class MovieDAO implements IDAO<Movie, Integer>{
        private final EntityManagerFactory emf;

        public MovieDAO(EntityManagerFactory emf) {
            this.emf = emf;
        }

        @Override
        public Movie create(Movie movie){
            try(EntityManager em = emf.createEntityManager()){
                em.getTransaction().begin();
                em.persist(movie);
                em.getTransaction().commit();
            }
            return null;
        }

        @Override
        public List<Movie> getAll(){

            try( EntityManager em = emf.createEntityManager()){

                TypedQuery<Movie> query =
                        em.createQuery("SELECT m FROM Movie m", Movie.class);
                return query.getResultList();
            }
        }


        public Movie getById(Integer id){

            try(EntityManager em = emf.createEntityManager()){
                return em.find(Movie.class, id);
            }
        }

        public Movie update(Movie movie){

            try(EntityManager em = emf.createEntityManager()){

                em.getTransaction().begin();
                Movie updateCourse = em.merge(movie);
                em.getTransaction().commit();
                return updateCourse;
            }
        }

        public boolean delete(Integer id){
            try(EntityManager em = emf.createEntityManager()){

                Movie movieDelete = em.find(Movie.class, id);
                if(movieDelete != null){
                    em.getTransaction().begin();
                    em.remove(movieDelete);
                    em.getTransaction().commit();
                    return true;
                }
                else{ return false; }
            }
        }
    }




