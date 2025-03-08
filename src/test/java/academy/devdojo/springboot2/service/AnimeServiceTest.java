package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.controllers.AnimeController;
import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.AnimePutRequestBodyCreator;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) //Usando Spring com jUnit
class AnimeServiceTest {
        @InjectMocks //Usa quando quer testar a classe em si
        private AnimeService animeService;

        @Mock //Usa pra todas as classes que estao sendo usadas no Controller eg. DateUtil e AnimeService
        private AnimeRepository animeRepositoryMock;

        @BeforeEach
            //Antes de todos os testes faça isso
        void setup(){
            PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
            //Mockando o Comportamentos dos Metodos do Service
            BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                    .thenReturn(animePage);
            BDDMockito.when(animeRepositoryMock.findAll())
                    .thenReturn(List.of(AnimeCreator.createValidAnime()));
            BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(AnimeCreator.createValidAnime()));
            BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                    .thenReturn(List.of(AnimeCreator.createValidAnime()));
            BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                    .thenReturn(AnimeCreator.createValidAnime());
            //Quando o metodo retorna void se usa o doNothing
            BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

        }
        @Test
        @DisplayName("Lists returns list of anime inside page object when successful")
        void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
            String expectedName = AnimeCreator.createValidAnime().getName();
            Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));
            Assertions.assertThat(animePage).isNotNull();
            Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
            Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName).isNotNull();
        }
        @Test
        @DisplayName("ListAll returns list of anime when successful")
        void listAllNonPageable_ReturnsListOfAnimes_WhenSuccessful(){
            String expectedName = AnimeCreator.createValidAnime().getName();
            List<Anime> animes = animeService.listAllNonPageable();
            Assertions.assertThat(animes)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(1);
            Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName).isNotNull();

        }
        @Test
        @DisplayName("findByIdOrThrowBadRequest returns anime when successful")
        void findById_ReturnsAnime_WhenSuccessful(){
            Long expectedId = AnimeCreator.createValidAnime().getId();
            Anime anime = animeService.findByIdOrThrowBadRequest(1);
            Assertions.assertThat(anime)
                    .isNotNull();
            Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);

        }
        @Test
        @DisplayName("findByIdOrThrowBadRequest throws BadRequestException when anime is not found")
        void findById_ThrowsBadRequestException_WhenAnimeIsNotFound(){
            BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());
            Assertions.assertThatThrownBy(() -> animeService.findByIdOrThrowBadRequest(1))
                    .isInstanceOf(BadRequestException.class); //tipo da excecao


        }
        @Test
        @DisplayName("FindByName returns List of anime when successful")
        void findByName_ReturnsAnime_WhenSuccessful(){
            String expectedName = AnimeCreator.createValidAnime().getName();
            List<Anime> animes = animeService.findByName("anime");
            Assertions.assertThat(animes)
                    .isNotNull()
                    .isNotEmpty();
            Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName).isNotNull();
        }
        @Test
        @DisplayName("FindByName returns an empty list when anime is not found")
        void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){
            //Alterando Comportamento
            BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                    .thenReturn(Collections.emptyList());
            List<Anime> animes = animeService.findByName("qualquercoisa");
            Assertions.assertThat(animes)
                    .isNotNull()
                    .isEmpty();
        }
        @Test
        @DisplayName("Save returns anime when successful")
        void save_ReturnsAnime_WhenSuccessful(){
            Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());
            Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
        }
        @Test
        @DisplayName("Replace update anime when successful")
        void replace_UpdatesAnime_WhenSuccessful(){
            Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                    .doesNotThrowAnyException();
        }
        @Test
        @DisplayName("Delete removes anime when successful")
        void delete_RemovesAnime_WhenSuccessful(){
            Assertions.assertThatCode(() -> animeService.delete(1))
                    .doesNotThrowAnyException();
        }

    }