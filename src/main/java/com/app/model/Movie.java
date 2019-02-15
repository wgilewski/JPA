package com.app.model;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data

@Table(name = "Movies")
public class Movie
{
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private Genre genre;
    private BigDecimal price;
    private int duration;
    private LocalDate releaseDate;


}
