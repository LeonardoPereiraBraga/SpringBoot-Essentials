package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//no JpaRepository passa o nome do objecto e o tipo do ID dele
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    // Ele faz sozinho o metodo se o nome do atributo for o mesmo do dominio
    List<Anime> findByName(String name);

}
