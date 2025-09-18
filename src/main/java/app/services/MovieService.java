package app.services;

import app.dtos.MovieIdOnlyDTO;
import app.dtos.GenreDTO;
import app.dtos.MovieListIdDTO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static app.services.MovieAPIService.getMovieIdByPeriodAndCountry;
import static java.util.Arrays.stream;

public class MovieService {

    List<MovieIdOnlyDTO> moviesIdOnly = getMovieIdByPeriodAndCountry("2025-08-01","2025-09-15","da");

    public Set<Integer> getAllMovieGenresByID(List<MovieIdOnlyDTO> movies){


    //    MovieIdOnlyDTO film = movies.get(0);
     //   Integer inte =film.getGenreIds().get(0);
     //  System.out.println(film.getGenreIds().toString());

      //  Integer [] integers = movies.get(0).getGenreIds().toArray(new Integer[0]);
    /*    Set<Integer> streamIdFromMovieList = movies.stream()
                .map(MovieIdOnlyDTO::getGenreIds) // Stream<List<Integer>>
                .flatMap(List::stream) // Flader Stream<List<Integer>> til Stream<Integer>
                .collect(Collectors.toSet());  //  Samler resultaterne i et Set  */

        Set<Integer> streamIdFromMovieList = movies.stream()
                .map(MovieIdOnlyDTO::getGenreIds) // Stream<List<Integer>>
                .filter(Objects::nonNull)         // sikkerhed mod null
                .flatMap(List::stream)            // Stream<Integer>
                .collect(Collectors.toCollection(LinkedHashSet::new));   // Set<Integer>, plus it helps testing by having the results in cronological order

       for(Integer i: streamIdFromMovieList) {System.out.println(i);}
        return streamIdFromMovieList;
    }

    public void getGenres(){

        getAllMovieGenresByID(moviesIdOnly);
    }

}
