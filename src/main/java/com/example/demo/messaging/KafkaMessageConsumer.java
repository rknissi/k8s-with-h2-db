package com.example.demo.messaging;

import com.example.demo.application.PersonApplication;
import com.example.demo.person.data.Person;
import com.example.demo.person.data.PersonAvro;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaMessageConsumer {

    @Autowired
    private PersonApplication personApplication;
    private ObjectMapper objectMapper = new ObjectMapper();

    //If you want manual commits
    //public void listenGroupFoo(PersonAvro person, Acknowledgment ack) throws JsonProcessingException {
    //ack.nack(Duration.ofSeconds(5));
    //ack.acknowledge();

    @KafkaListener(topics = "personCreation", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupFoo(PersonAvro person) throws JsonProcessingException {
        System.out.println("Received message from Kafka: " + person);
        if (person.getAge() == 0l) {
            System.out.println("Will not ack: " + person);
            throw new RuntimeException(person.toString());
        } else {
            Person newPerson = new Person();
            newPerson.setName(person.getName().toString());
            newPerson.setAge(person.getAge());
            personApplication.createPerson(newPerson);
            System.out.println("Will ack: " + person);
        }
    }

    @KafkaListener(topics = "personCreation.DLT")
    public void listenGroupDLT(PersonAvro person) throws JsonProcessingException {
        System.out.println("Received message from DLT: " + person);
    }

    @DltHandler
    public void dlt(PersonAvro person, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println("Received message from DLT handler: " + person);
    }
}
