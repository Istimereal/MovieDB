package app.entities;
import app.entities.Movie;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    String name;

// m:m relationer
    @ManyToMany (cascade = CascadeType.PERSIST)
    private Set<Movie> movies = new HashSet<>();

    // Uni-directonal add
    public void addMovie(Movie movie){
        movies.add(movie);
     }


 /*   // Bi-directional udpate
    public void addMovie(Movie movie) {
        this.movies.add(movie);
        if(movies != null){
            movie.setActor(this);
        }
    }    */


}
