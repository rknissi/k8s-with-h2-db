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
public class PersonCreationReceiver {

    @Autowired
    private PersonApplication personApplication;

    private ObjectMapper objectMapper = new ObjectMapper();


    @RabbitListener(queues = "personCreationQueue")
    public void receive(String person) throws JsonProcessingException {
        System.out.println("Creating person from RabbitMQ:" + person);
        Person newPerson = objectMapper.readValue(person, Person.class);
        if (newPerson.getAge() == 0l) {
            System.out.println("Error from RabbitMQ:" + person);
            throw new RuntimeException("create.error");
        } else {
            System.out.println("Created from RabbitMQ:" + person);
            Person createdPerson = personApplication.createPerson(newPerson);
        }
    }

    @RabbitListener(queues = "personCreationDLQ")
    public void receiveDLQ(String person) throws JsonProcessingException {
        System.out.println("Received Person from DLQ:" + person);
    }

}
