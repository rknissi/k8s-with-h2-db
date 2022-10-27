package com.example.demoh2.messaging;

import com.example.demoh2.application.PersonApplication;
import com.example.demoh2.data.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaMessageConsumer {

    @Autowired
    private PersonApplication personApplication;
    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "personCreation")
    public void listenGroupFoo(String person) throws JsonProcessingException {
        System.out.println("received message: " + person);
        personApplication.createPerson(objectMapper.readValue(person, Person.class));
    }
}
