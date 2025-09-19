package app;

import app.dtos.MovieIdOnlyDTO;
import app.services.MovieService;

import java.util.List;

import static app.services.MovieAPIService.getMovieIdByPeriodAndCountry;

public class Main {
    public static void main(String[] args) {

        MovieService movieService = new MovieService();

        movieService.getMovieValues();
    }
}