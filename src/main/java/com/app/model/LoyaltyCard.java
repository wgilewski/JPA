package com.app.model;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

@Table(name = "Loyalty_Cards")
public class LoyaltyCard
{
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate expirationDate;
    private BigDecimal discount;
    private int moviesWithDiscount;

    @OneToOne(mappedBy = "loyaltyCard")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Customer customer;


}
