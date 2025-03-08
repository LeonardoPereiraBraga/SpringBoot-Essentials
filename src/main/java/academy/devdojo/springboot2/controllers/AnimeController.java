package academy.devdojo.springboot2.controllers;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.DateUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController // indica que os métodos na classe devem retornar dados diretamente,ex: JSON
@RequestMapping("animes") //Todas as rotas em animecontroller vai ter o prefixo /anime
@RequiredArgsConstructor //Cria construtor dos atributos que sao final
public class AnimeController {
    private final AnimeService animeService;
//    private final DateUtil dateUtil; //Mesmo que DateUtil dateUtil = new DateUtil()


    @GetMapping//Requisicao do tipo GET
    public ResponseEntity<Page<Anime>> list(Pageable pageable){ //ResponseEntity serve pra passar o StatusCode
        return new ResponseEntity<>(animeService.listAll(pageable), HttpStatus.OK);
    }
    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll(){ //ResponseEntity serve pra passar o StatusCode
        return new ResponseEntity<>(animeService.listAllNonPageable(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")//Requisicao do tipo GET
    public ResponseEntity<Anime> findById(@PathVariable long id){ //Pegando a variavel dos Params
        return new ResponseEntity<>(animeService.findByIdOrThrowBadRequest(id), HttpStatus.OK);
    }
    @GetMapping(path = "/find") //http://localhost:8080/animes/find?name=frieren
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){ //Pegando a variavel dos Params
        return new ResponseEntity<>(animeService.findByName(name), HttpStatus.OK);
    }

    @PostMapping                                  //@Valid pra usar as validacoes
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody anime){
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
