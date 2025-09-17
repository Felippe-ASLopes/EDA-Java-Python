package com._XKO.produtor.service;

import com._XKO.produtor.config.RabbitMQConfig;
import com._XKO.produtor.entity.Personagem;
import com._XKO.produtor.repository.PersonagemRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonagemService {

    @Autowired
    private PersonagemRepository personagemRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Map<String, Object> create(Personagem personagem) {
        personagemRepository.save(personagem);

        System.out.println("Enviando personagem para a fila: " + personagem.getNome());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.exchangeName,
                RabbitMQConfig.routingKey,
                "Personagem Criado: " + personagem.getNome() + " - " + personagem.getApelido()
        );

        var response = new HashMap<String, Object>();
        response.put("mensagem", "Personagem cadastrado com sucesso.");
        response.put("personagem", personagem);
        return response;
    }

    public List<Personagem> list() {
        return personagemRepository.findAll();
    }
}
