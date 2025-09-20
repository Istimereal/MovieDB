package app.entities;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer populatity;

}
