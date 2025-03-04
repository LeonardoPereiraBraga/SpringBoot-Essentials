package academy.devdojo.springboot2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data //Getters Setters hashcode equals
@AllArgsConstructor //Construtor com todos os atributos
public class Anime {
    private Long id;
    private String name;

}
