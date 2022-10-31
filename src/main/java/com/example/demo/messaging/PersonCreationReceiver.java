package com.example.demo.messaging;

import com.example.demo.application.PersonApplication;
import com.example.demo.person.data.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("!test")
@RabbitListener(queues = "personCreationQueue")
public class PersonCreationReceiver {

    @Autowired
    private PersonApplication personApplication;

    private ObjectMapper objectMapper = new ObjectMapper();


    @RabbitHandler
    public void receive(String person) throws JsonProcessingException {
        Person createdPerson = personApplication.createPerson(objectMapper.readValue(person, Person.class));
    }
}
