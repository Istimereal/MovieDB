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

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import static app.services.MovieAPIService.getGenreNamesFromGenreID;
import static app.services.MovieAPIService.getMovieIdByPeriodAndCountry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MoviePersistService {

    List<MovieIdOnlyDTO> moviesIdOnly = getMovieIdByPeriodAndCountry("2025-08-01", "2025-09-15", "da");
    List<GenreDTO> allDTOGenres = getGenreNamesFromGenreID("da");
    List<Genre> allGenres =   createGenre(allDTOGenres);
    List<String> allMovieID = MovieService.getMovieIds(moviesIdOnly);
    List<MovieCompleteInfoDTO> allMoviesCompleteInfo = MovieService.getAllMoviesCompleteInfo(allMovieID, "da");

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public void persistEntitiesToDB() {

        MovieService.getAllMovieGenresByID(moviesIdOnly);
        MovieService.getMovieIds(moviesIdOnly);
        //   createActor(allMoviesCompleteInfo.get(0));   Just a test
        System.out.println("Ready to persist entities to DB \n");
        persistGenresToDB(allGenres);
        System.out.println("Genres persisted \n");
        persistMoviesToDB();
        System.out.println("Movies persisted \n");
        persistActorsToDB();
        System.out.println("Actors persisted \n");
        persistDirestorsToDB();
        System.out.println("Directors persisted \n");
    }

    public void persistMoviesToDB() {

        List<Movie> allMovies = new ArrayList<>();
        MovieDAO movieDAO = new MovieDAO(emf);

        try {
            for (MovieCompleteInfoDTO mDTO : allMoviesCompleteInfo) {

                Movie m = createMovie(mDTO);

                allMovies.add(m);
            }
        } catch (NullPointerException n) {
            System.out.println("Der skete en fejl ved oprettelse af film");
        }

        try {
            List<Movie> persist = movieDAO.createAll(allMovies);
            System.out.println("Filmene er gemt i Databasen!");
        } catch (PersistenceException e) {
            System.out.println("JPA fejl: kunne ikke gemme filmene");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Fejl i entiteterne: tjek input");
            e.printStackTrace();
        }
    }

public void persistActorsToDB() {

    //   List<ActorDAO> allActors = new ArrayList<>();
    List<Actor> allActors = new ArrayList<>();

    ActorDAO actorDAO = new ActorDAO(emf);

    try {
        for (int i = 0; i < allMoviesCompleteInfo.size(); i++) {

            List<Actor> actorOneMovies = createActor(allMoviesCompleteInfo.get(i));
            for (Actor actor : actorOneMovies) {
                allActors.add(actor);
            }
        }
    }
    catch (NullPointerException n) {
        System.out.println("Der skete en fejl ved oprettelse af Actors");
    }

    try {
        List<Actor> persist = actorDAO.createAll(allActors);
        System.out.println("Filmene er gemt i Databasen!");
    } catch (PersistenceException e) {
        System.out.println("JPA fejl: Kunne ikke gemme Actors");
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        System.out.println("Fejl i entiteterne: tjek input");
        e.printStackTrace();
    }
}

    public void persistDirestorsToDB(){

        DirectorDAO directorDAO = new DirectorDAO(emf);

        List<Director> allDirectors = getDirectordFromAllMovies();
        try {
            List<Director> persist = directorDAO.createAll(allDirectors);
            System.out.println("Filmene er gemt i Databasen!");
        } catch (PersistenceException e) {
            System.out.println("JPA fejl: Kunne ikke gemme Directors");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Fejl i entiteterne: tjek input");
            e.printStackTrace();
        }
    }

    public void persistGenresToDB(List<Genre> genreList) {

        List<Genre> genres = new ArrayList<>();

        GenreDAO genreDAO = new GenreDAO(emf);

            try {
                List<Genre> persist = genreDAO.createAll(genreList);
                System.out.println("Filmene er gemt i Databasen!");
            } catch (PersistenceException e) {
                System.out.println("JPA fejl: Kunne ikke gemme Genre");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println("Fejl i entiteterne: tjek input");
                e.printStackTrace();
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

    public List<Director> getDirectordFromAllMovies(){

        List<Director> allDirectors = new ArrayList<>();

        try {
            for (int i = 0; i < allMoviesCompleteInfo.size(); i++) {

                List<Director> director = createDirector(allMoviesCompleteInfo.get(i));
                for (Director d : director) {
                    if (d != null) {
                        System.out.println("Director NR: " + i + " toString: " + director.toString());

                        allDirectors.add(d);
                    }
                }
            }
        }
        catch (NullPointerException n) {
            System.out.println("Der skete en fejl ved oprettelse af Directors");
        }
        return allDirectors;
    }

    public List<Director> createDirector(MovieCompleteInfoDTO movieCompleteInfoDTO) {

       List<MovieCompleteInfoDTO.Crew> crew = movieCompleteInfoDTO.getCredits().getCrew();
       List<Director> directorsInMovie = new ArrayList<>();
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

                    directorsInMovie.add(director);
                }
            }
        }
       return directorsInMovie;
    }

    public List<Genre> createGenre(List<GenreDTO> genreDTOList) {

        List<Genre> genres = new ArrayList<>();

        try {
            for (GenreDTO genreDTO : genreDTOList) {
                int id = genreDTO.getIdTMDB();
                String genreName = genreDTO.getName();
                Genre genre = Genre.builder()
                        .name(genreName)
                        .IdTMDB(id)
                        .build();
                genres.add(genre);
            }
        } catch (NullPointerException n) {
            System.out.println("Der skete en fejl ved oprettelse af Genre");
        }
        return genres;
    }
}
