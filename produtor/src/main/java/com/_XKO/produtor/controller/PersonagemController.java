package com._XKO.produtor.controller;

import com._XKO.produtor.entity.Personagem;
import com._XKO.produtor.service.PersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/personagens")
public class PersonagemController {

    @Autowired
    private PersonagemService personagemService;

    @PostMapping
    public ResponseEntity<Object> createPersonagem(@RequestBody Personagem personagem) {
        Object json = personagemService.create(personagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<List<Personagem>> listPersonagem() {
        List<Personagem> personagens = personagemService.list();
        return personagens.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(personagens);
    }
}
