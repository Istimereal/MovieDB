package app.services;

import app.dtos.MovieIdOnlyDTO;
import app.dtos.MovieListIdDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MovieAPIService {
// https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&release_date.gte=2025-08-01&release_date.lte=2025-09-15&sort_by=popularity.desc&with_original_language=da
   // public static

    public static List<MovieIdOnlyDTO> getMovieIdByPeriodAndCountry(String periodStart, String periodEnd, String countryCode) {

        MovieListIdDTO movieIDWrapper = null;
        final String apiKey = System.getenv("ApiKey");

String uri = "https://api.themoviedb.org/3/discover/movie?" + apiKey + "&include_adult=false&include_video=false&language=en-US&page=1&release_date.gte=" + periodStart +
        "&release_date.lte=" + periodEnd + "&sort_by=popularity.desc&with_original_language=" + countryCode;

       // System.out.println(uri);

        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();


        try {
     HttpRequest request = HttpRequest.newBuilder()
             .uri(new URI(uri))
             .GET()
             .build();

     HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

     if(response.statusCode() == 200){

         Object jSon = response.body();

         System.out.println(jSon.toString());

         movieIDWrapper = objectMapper.readValue(jSon.toString(), MovieListIdDTO.class);

         for(MovieIdOnlyDTO m : movieIDWrapper.getMovieIdDTOList()){

             System.out.println(m);
         }
     }
     else{
         System.out.println("Fejl ved hentning fra film API");
     }
 }
 catch (Exception e){
     e.printStackTrace();
 }

return movieIDWrapper.getMovieIdDTOList();
    }
}
