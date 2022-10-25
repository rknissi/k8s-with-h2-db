package com.example.demoh2.application;

import com.example.demoh2.data.Person;
import com.example.demoh2.repository.PersonRepository;
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
