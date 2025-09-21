package app;

import app.dtos.MovieIdOnlyDTO;
import app.services.MoviePersistService;

import java.util.List;

import static app.services.MovieAPIService.getMovieIdByPeriodAndCountry;

public class Main {
    public static void main(String[] args) {

        MoviePersistService movieService = new MoviePersistService();

        movieService.persistEntitiesToDB();
    }
}