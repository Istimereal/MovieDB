package app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MovieCompleteInfoDTO {

    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("belongs_to_collection")
    private Object belongsToCollection;

    private int budget;
    private List<Genre> genres;
    private String homepage;
    private int id;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("origin_country")
    private List<String> originCountry;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    private String overview;
    private double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;

    @JsonProperty("production_countries")
    private List<ProductionCountry> productionCountries;

    @JsonProperty("release_date")
    private String releaseDate;

    private long revenue;
    private int runtime;

    @JsonProperty("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;

    private String status;
    private String tagline;
    private String title;
    private boolean video;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    private Credits credits;

    @Data
    public static class Genre {
        private int id;
        private String name;
    }

    @Data
    public static class ProductionCompany {
        private int id;

        @JsonProperty("logo_path")
        private String logoPath;

        private String name;

        @JsonProperty("origin_country")
        private String originCountry;
    }

    @Data
    public static class ProductionCountry {
        @JsonProperty("iso_3166_1")
        private String iso3166;

        private String name;
    }

    @Data
    public static class SpokenLanguage {
        @JsonProperty("english_name")
        private String englishName;

        @JsonProperty("iso_639_1")
        private String iso639;

        private String name;
    }

    @Data
    public static class Credits {
        private List<Cast> cast;
        private List<Crew> crew;
    }

    @Data
    public static class Cast {
        private boolean adult;
        private int gender;
        private int id;

        @JsonProperty("known_for_department")
        private String knownForDepartment;

        private String name;

        @JsonProperty("original_name")
        private String originalName;

        private double popularity;

        @JsonProperty("profile_path")
        private String profilePath;

        @JsonProperty("cast_id")
        private int castId;

        private String character;

        @JsonProperty("credit_id")
        private String creditId;

        private int order;
    }

    @Data
    public static class Crew {
        private boolean adult;
        private int gender;
        private int id;

        @JsonProperty("known_for_department")
        private String knownForDepartment;

        private String name;

        @JsonProperty("original_name")
        private String originalName;

        private double popularity;

        @JsonProperty("profile_path")
        private String profilePath;

        @JsonProperty("credit_id")
        private String creditId;

        private String department;
        private String job;
    }
}
