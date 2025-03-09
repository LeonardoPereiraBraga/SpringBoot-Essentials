package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.AnimePutRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;

//Teste de Integraçao - sua diferença é que ele sobe o servidor inteiro
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Portal aleatoria
@AutoConfigureTestDatabase
//Nessa anotacao pra cada teste vai ser dropado e criado uma nova database
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AnimeRepository animeRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Lists returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();
        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName).isNotNull();
    }
    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();
        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes).isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName).isNotNull();

    }
    @Test
    @DisplayName("FindById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        Long expectedId = savedAnime.getId();
        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }
    @Test
    @DisplayName("FindByName returns List of anime when successful")
    void findByName_ReturnsAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);
        List<Anime> animes =  testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty();
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName).isNotNull();
    }
    @Test
    @DisplayName("FindByName returns an empty list when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){
        List<Anime> animes =  testRestTemplate.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }
    @Test
    @DisplayName("Save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }
    @Test
    @DisplayName("Replace update anime when successful")
    void replace_UpdatesAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        savedAnime.setName("new name");
        //AnimePutRequestBody animePutRequestBody = AnimePutRequestBodyCreator.createAnimePutRequestBody();
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes",
                HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        //AnimePutRequestBody animePutRequestBody = AnimePutRequestBodyCreator.createAnimePutRequestBody();
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE, null, Void.class, savedAnime.getId());
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
