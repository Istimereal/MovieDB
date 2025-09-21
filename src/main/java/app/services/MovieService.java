package app.services;

import app.dtos.MovieCompleteInfoDTO;
import app.dtos.MovieIdOnlyDTO;
import app.dtos.GenreDTO;
import app.dtos.MovieListIdDTO;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static app.services.MovieAPIService.*;
import static java.util.Arrays.stream;

public class MovieService {

    public static Set<Integer> getAllMovieGenresByID(List<MovieIdOnlyDTO> movies){

        Set<Integer> streamIdFromMovieList = movies.stream()
                .map(MovieIdOnlyDTO::getGenreIds) // Stream<List<Integer>>
                .filter(Objects::nonNull)         // sikkerhed mod null
                .flatMap(List::stream)            // Stream<Integer>
                .collect(Collectors.toCollection(LinkedHashSet::new));   // Set<Integer>, plus it helps testing by having the results in cronological order

  //     for(Integer i: streamIdFromMovieList) {System.out.println(i);}  // debug
        return streamIdFromMovieList;
    }

    public static List<String> getMovieIds(List<MovieIdOnlyDTO> initialMovies){

        List<String> movieIds = new ArrayList<>();

        for(MovieIdOnlyDTO movie: initialMovies) {
           Integer id = movie.getId();
           String movieID = Integer.toString(id);
      //      System.out.println("String movieId: " + movieID);
           movieIds.add(movieID);
        }
        return movieIds;
    }

    public static List<MovieCompleteInfoDTO> getAllMoviesCompleteInfo(List<String> movieId, String language){

        List<MovieCompleteInfoDTO> allMovies = new ArrayList<>();
        MovieCompleteInfoDTO movieCompleteInfoDTO = null;

        for(String id : movieId){

            movieCompleteInfoDTO = getMovieCompleteInfo(id, language);

            allMovies.add(movieCompleteInfoDTO);
        }
        return allMovies;
    }
}
