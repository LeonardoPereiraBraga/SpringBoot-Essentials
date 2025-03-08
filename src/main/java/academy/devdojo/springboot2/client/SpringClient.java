package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SpringClient {
    public static void main(String[] args) {
        //Requisicao
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class,2);
        Anime Object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        ResponseEntity<List<Anime>> animes = new RestTemplate().exchange("http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        });
        System.out.println(entity);
        System.out.println(Object);
        System.out.println(animes.getBody());
        Anime kingdom = Anime.builder().name("Kingdom").build();
        Anime kingdomsaved = new RestTemplate().postForObject("http://localhost:8080/animes", kingdom, Anime.class);
        System.out.println(kingdomsaved);
        Anime samuraiChamploo = Anime.builder().name("Samurai Champloo").build();
        ResponseEntity<Anime> samuraiChamplooSaved = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.POST,
                new HttpEntity<>(samuraiChamploo, createJsonHeader()),
                Anime.class);

        Anime animeSaved = samuraiChamplooSaved.getBody();
        ResponseEntity<Void> samuraiChamploUpdated = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.PUT,
                new HttpEntity<>(animeSaved, createJsonHeader()),
                Void.class);

        System.out.println("Updated" + samuraiChamploUpdated);
        animeSaved.setName("Samurai Champloo 2");
        ResponseEntity<Void> samuraiChamploDeleted = new RestTemplate().exchange("http://localhost:8080/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeSaved.getId());

    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
