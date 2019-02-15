package com.app.model;
import lombok.*;
import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data

@Table(name = "Customers")
public class Customer
{

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private String email;
    private int age;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loyaltyCard_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private LoyaltyCard loyaltyCard;


}
