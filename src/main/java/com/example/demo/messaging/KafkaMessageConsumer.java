package com.example.demo.messaging;

import com.example.demo.application.PersonApplication;
import com.example.demo.person.data.Person;
import com.example.demo.person.data.PersonAvro;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("!test")
public class KafkaMessageConsumer {

    @Autowired
    private PersonApplication personApplication;
    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "personCreation")
    public void listenGroupFoo(PersonAvro person) throws JsonProcessingException {
        System.out.println("received message: " + person);
        Person newPerson = new Person();
        newPerson.setName(person.getName().toString());
        newPerson.setAge(person.getAge());
        //personApplication.createPerson(objectMapper.readValue(person, Person.class));
        personApplication.createPerson(newPerson);
    }
}
