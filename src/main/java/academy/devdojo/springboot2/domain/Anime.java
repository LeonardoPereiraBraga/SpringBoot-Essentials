package academy.devdojo.springboot2.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data //Getters Setters hashcode equals
@AllArgsConstructor //Construtor com todos os atributos
@NoArgsConstructor //Construtor vazio pro JPA funcionar
@Entity //Jpa
@Builder
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The anime cannot be empty")
    private String name;

}
