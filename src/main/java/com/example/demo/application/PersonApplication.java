package com.example.demo.application;

import com.example.demo.person.data.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonApplication {

    @Autowired
    private PersonRepository personRepository;

    public Person getPerson(String id) {
        Optional<Person> optionalPerson = personRepository.findById(Long.parseLong(id));
        if (optionalPerson.isPresent()) {
            return optionalPerson.get();
        } else {
            return null;
        }
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

}
