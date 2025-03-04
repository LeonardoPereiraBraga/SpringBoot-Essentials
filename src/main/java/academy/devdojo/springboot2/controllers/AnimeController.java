package academy.devdojo.springboot2.controllers;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController // indica que os métodos na classe devem retornar dados diretamente,ex: JSON
@RequestMapping("animes") //Todas as rotas em animecontroller vai ter o prefixo /anime
@RequiredArgsConstructor //Cria construtor dos atributos que sao final
public class AnimeController {
    private final AnimeService animeService;
    private final DateUtil dateUtil; //Mesmo que DateUtil dateUtil = new DateUtil()


    @GetMapping//Requisicao do tipo GET
    public ResponseEntity<List<Anime>> list(){ //ResponseEntity serve pra passar o StatusCode
        System.out.println(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")//Requisicao do tipo GET
    public ResponseEntity<Anime> findById(@PathVariable long id){ //Pegando a variavel dos Params
        return new ResponseEntity<>(animeService.findById(id), HttpStatus.OK);
    }
}
