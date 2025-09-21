package app.entities;
import app.entities.*;

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
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
    @Column(nullable = false)
private String title;
    @Column(nullable = false)
private String releaseDate;
    @Column(nullable = false)
private String description;
    @Column(nullable = false)
    private double populatity;

 /*   @ManyToMany
    @ToString.Exclude
    @JoinColumn(name = "movie_id", nullable = true)
    // @EqualsAndHashCode.Exclude
    private Set<Actor> actors = new HashSet<>();  */

  /*  @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Actor> actors = new HashSet<>();

    public void addActor(Actor actor) {
        this.actors.add(actor);
        if (actor != null) {
            actor.setActor(this);
        }
    }

    @OneToMany
    @ToString.Exclude
    private Actor actor;  */

}
