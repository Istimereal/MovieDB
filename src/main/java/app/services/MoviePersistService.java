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
import java.util.Locale;

public class MoviePersistService {

    List<MovieIdOnlyDTO> moviesIdOnly = getMovieIdByPeriodAndCountry("2025-08-01", "2025-09-15", "da");
    List<GenreDTO> allDTOGenres = getGenreNamesFromGenreID("da");
    List<String> allMovieID = MovieService.getMovieIds(moviesIdOnly);
    List<MovieCompleteInfoDTO> allMoviesCompleteInfo = MovieService.getAllMoviesCompleteInfo(allMovieID, "da");

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public void persistEntitiesToDB() {

        MovieService.getAllMovieGenresByID(moviesIdOnly);
        MovieService.getMovieIds(moviesIdOnly);
     //   createActor(allMoviesCompleteInfo.get(0));   Just a test
        System.out.println("Ready to persist entities to DB \n");
        persistGenresToDB(allDTOGenres);
        System.out.println("Genres persisted \n");
        persistMoviesToDB();
        System.out.println("Movies persisted \n");
        persistActorsToDB();
        System.out.println("Actors persisted \n");
        persistDirestorsToDB();
        System.out.println("Directors persisted \n");
    }

    public void persistMoviesToDB(){

     //   List<MovieDAO> movies = new ArrayList<>();
        List<Movie> allMovies = new ArrayList<>();
        MovieDAO movieDAO = new MovieDAO(emf);

try(EntityManager em = emf.createEntityManager()) {
    for (MovieCompleteInfoDTO mDTO : allMoviesCompleteInfo) {

        Movie m = createMovie(mDTO);

        allMovies.add(m);

    //    movieDAO.create(m);
    //    movies.add(movieDAO);
    }
em.getTransaction().begin();
   // em.persist(movies);    Kan den bruges i stedet, og hvorfor er forEach bedre
    allMovies.forEach(em::persist);
    em.getTransaction().commit();
}
    }

    public void persistActorsToDB(){

     //   List<ActorDAO> allActors = new ArrayList<>();
        List<Actor> allActors = new ArrayList<>();

        ActorDAO actorDAO = new ActorDAO(emf);

        try(EntityManager em = emf.createEntityManager()) {
            for (int i = 0; i < allMoviesCompleteInfo.size(); i++) {

            List<Actor> actorOneMovies = createActor(allMoviesCompleteInfo.get(i));
                for(Actor actor : actorOneMovies){

                    allActors.add(actor);
                }

           /*     for (Actor a : actors) {

                   actorDAO.create(a);
                     allActors.add(actorDAO);
                } */
            }
            em.getTransaction().begin();
            allActors.forEach(em::persist);
            em.getTransaction().commit();
        }
    }

    public void persistDirestorsToDB(){

      //  List<DirectorDAO> allDirectors = new ArrayList<>();
        List<Director> allDirectors = new ArrayList<>();

      //  DirectorDAO directorDAO = new DirectorDAO(emf);

        try(EntityManager em = emf.createEntityManager()){

            for (int i = 0; i < allMoviesCompleteInfo.size(); i++) {

                Director director = createDirector(allMoviesCompleteInfo.get(i));

                if (director != null) {
                    System.out.println("Director NR: " + i + " toString: " + director.toString());

                    allDirectors.add(director);
                    //   directorDAO.create(director);
                    //  allDirectors.add(directorDAO);
                }
            }

            em.getTransaction().begin();
            allDirectors.forEach(em::persist);
            em.getTransaction().commit();
        }
    }

    public void persistGenresToDB(List<GenreDTO> genreDTOList) {

        List<Genre> genres = new ArrayList<>();

        try (EntityManager em = emf.createEntityManager()) {

            for (GenreDTO genreDTO : genreDTOList) {
                int id = genreDTO.getIdTMDB();
                String genreName = genreDTO.getName();
                Genre genre = Genre.builder()
                        .name(genreName)
                        .IdTMDB(id)
                        .build();
                genres.add(genre);

                em.getTransaction().begin();
                genres.forEach(em::persist);
                em.getTransaction().commit();
            }
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
        System.out.println("Længde liste med crew: " + crew.size());


        Director director = null;
        if(crew != null) {
            for (int i = 0; i < crew.size(); i++) {
                System.out.println("Crew nummer: " + i + " Crew getJob: " + crew.get(i).getJob());
                if (crew.get(i).getJob().toLowerCase(Locale.ROOT).contains("director")) {

                    String name = crew.get(i).getName();

                    director = Director.builder()
                            .name(name)
                            .build();
                }
            }
        }
       return director;
    }

    public List<Genre> createGenre(List<GenreDTO> genreDTOList) {

        List<Genre> genres = new ArrayList<>();

        for(GenreDTO genreDTO : genreDTOList){
            int id = genreDTO.getIdTMDB();
            String genreName = genreDTO.getName();
            Genre genre = Genre.builder()
                    .id(id)
                    .name(genreName)
                    .build();
          genres.add(genre);

            System.out.println("GenreName: " + genre.getName() + " GenreID: " + genre.getId());
        }
        return genres;
    }
}
