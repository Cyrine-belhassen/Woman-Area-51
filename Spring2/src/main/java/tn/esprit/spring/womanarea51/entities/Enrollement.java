package tn.esprit.spring.womanarea51.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Enrollement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    Subscription subscription;




    boolean renewable;
   @JsonFormat(pattern="dd-MM-yyyy")
    LocalDate created;



    @JsonFormat(pattern="dd-MM-yyyy")
    LocalDate expire;
}
