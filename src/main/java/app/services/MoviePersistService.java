package app.services;
import app.daos.ActorDAO;
import app.daos.DirectorDAO;
import app.daos.GenreDAO;
import app.daos.MovieDAO;
import app.dtos.GenreDTO;
import app.dtos.MovieCompleteInfoDTO;
import app.dtos.MovieIdOnlyDTO;
import app.entities.*;
import app.config.HibernateConfig;
import app.services.MovieService;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.dialect.function.json.AbstractJsonRemoveFunction;

import javax.naming.Name;

import static app.services.MovieAPIService.getGenreNamesFromGenreID;
import static app.services.MovieAPIService.getMovieIdByPeriodAndCountry;

import java.util.ArrayList;
import java.util.List;

public class MoviePersistService {

    List<MovieIdOnlyDTO> moviesIdOnly = getMovieIdByPeriodAndCountry("2025-08-01", "2025-09-15", "da");
    List<GenreDTO> allGenres = getGenreNamesFromGenreID("da");
    List<String> allMovieID = MovieService.getMovieIds(moviesIdOnly);
    List<MovieCompleteInfoDTO> allMoviesCompleteInfo = MovieService.getAllMoviesCompleteInfo(allMovieID, "da");

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public void persistEntitiesToDB() {

        MovieService.getAllMovieGenresByID(moviesIdOnly);
        MovieService.getMovieIds(moviesIdOnly);
        createActor(allMoviesCompleteInfo.get(0));
        persistGenresToDB(allGenres);
        persistMoviesToDB();
        persistActorsToDB();
    }

    public void persistMoviesToDB(){

        List<MovieDAO> movies = new ArrayList<>();
        MovieDAO movieDAO = new MovieDAO(emf);

try(EntityManager em = emf.createEntityManager()) {
    for (MovieCompleteInfoDTO mDTO : allMoviesCompleteInfo) {

        Movie m = createMovie(mDTO);

        movieDAO.create(m);
        movies.add(movieDAO);
    }
em.getTransaction().begin();
   // em.persist(movies);    Kan den bruges i stedet, oghvordor er forEach bedre
    movies.forEach(em::persist);
    em.getTransaction().commit();
}
    }

    public void persistActorsToDB(){

        List<ActorDAO> allActors = new ArrayList<>();
        List<Actor> actors;

        ActorDAO actorDAO = new ActorDAO(emf);

        try(EntityManager em = emf.createEntityManager()) {
            for (int i = 0; i < allMoviesCompleteInfo.size(); i++) {

                actors = createActor(allMoviesCompleteInfo.get(i));

                for (Actor a : actors) {

                    actorDAO.create(a);

                    allActors.add(actorDAO);
                }
            }
            em.getTransaction().begin();
            allActors.forEach(em::persist);
            em.getTransaction().commit();
        }
    }

    public void persistDirestorsToDB(){

        List<DirectorDAO> allDirectors = new ArrayList<>();

        DirectorDAO directorDAO = new DirectorDAO(emf);

        try(EntityManager em = emf.createEntityManager()){

            for (int i = 0; i < allMoviesCompleteInfo.size(); i++) {

               Director director = createDirector(allMoviesCompleteInfo.get(i));

               directorDAO.create(director);
               allDirectors.add(directorDAO);
            }

            em.getTransaction().begin();
            allDirectors.forEach(em::persist);
            em.getTransaction().commit();
        }
    }

    public void persistGenresToDB(List<GenreDTO> genresDTOs){
        List<GenreDAO> allGenres = new ArrayList<>();


        GenreDAO genreDAO = new GenreDAO(emf);

        try(EntityManager em = emf.createEntityManager()) {
            for (GenreDTO g : genresDTOs) {

                Genre genre = Genre.builder()
                    .id(g.getId())
                        .name(g.getName())
                        .build();

                allGenres.add(genreDAO);
            }
            em.getTransaction().begin();
            allGenres.forEach(em::persist);
            em.getTransaction().commit();
        }

    }


    public Movie createMovie(MovieCompleteInfoDTO movieCompleteInfoDTO) {

        String title = movieCompleteInfoDTO.getTitle();

        String releaseDate = movieCompleteInfoDTO.getReleaseDate();

        String description = movieCompleteInfoDTO.getOverview();

        double popularity = movieCompleteInfoDTO.getPopularity();

        Movie movie = Movie.builder()
                .title(title)
                .releaseDate(releaseDate)
                .description(description)
                .populatity(popularity)
                .build();
        return movie;
    }

    public List<Actor> createActor(MovieCompleteInfoDTO movieCompleteInfoDTO) {


        List<Actor> actors = new ArrayList<>();

        List<MovieCompleteInfoDTO.Cast> cast = movieCompleteInfoDTO.getCredits().getCast();

        for (int i = 0; i < cast.size(); i++) {

            String name = cast.get(i).getName();

          Actor actor1 = Actor.builder()
                  .name(name)
                  .build();

          actors.add(actor1);
        }
            return actors;
    }

    public Director createDirector(MovieCompleteInfoDTO movieCompleteInfoDTO) {
       List<MovieCompleteInfoDTO.Crew> crew = movieCompleteInfoDTO.getCredits().getCrew();

        Director director = null;
       for (int i = 0; i < crew.size(); i++) {

           if(crew.get(i).getJob().equals("director")){

               String name = crew.get(i).getName();

               director = Director.builder()
                       .name(name)
                       .build();
           }
       }
       return director;
    }

    public List<Genre> createGenre(List<GenreDTO> genreDTOList) {

        List<Genre> genres = new ArrayList<>();

        for(GenreDTO genreDTO : genreDTOList){
            int id = genreDTO.getId();
            String genreName = genreDTO.getName();
            Genre genre = Genre.builder()
                    .id(id)
                    .name(genreName)
                    .build();
          genres.add(genre);
        }
        return genres;
    }
}
