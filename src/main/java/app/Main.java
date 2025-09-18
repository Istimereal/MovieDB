package app;

import app.dtos.MovieIdOnlyDTO;

import java.util.List;

import static app.services.MovieIdFromReleasePeriod.getMovieIdByPeriodAndCountry;

public class Main {
    public static void main(String[] args) {

        List<MovieIdOnlyDTO> moviesIdOnlies = getMovieIdByPeriodAndCountry("2025-08-01","2025-09-15","da");

    }
}