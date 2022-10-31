package com.example.demo.endpoint;

import com.example.demo.application.PersonApplication;
import com.example.demo.person.data.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonEndpoint {

    @Autowired
    private PersonApplication personApplication;

    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<String> check() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/person/{id}")
    @ResponseBody
    public ResponseEntity<Person> getPerson(@PathVariable("id") String id) {
        Person person = personApplication.getPerson(id);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    @PostMapping("/person")
    @ResponseBody
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person createdPerson = personApplication.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }
}
