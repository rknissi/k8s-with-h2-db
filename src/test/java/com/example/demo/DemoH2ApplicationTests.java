package com.example.demo;

import com.example.demo.person.data.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class DemoH2ApplicationTests {

	@Autowired
	private MockMvc mvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void contextLoads() {
	}

	@Test
	void checkIfSystemIsOn() throws Exception {
		mvc.perform(get("/check")).andExpect(status().isOk());
	}

	@Test
	void check404IfNotFound() throws Exception {
		mvc.perform(get("/person/10000")).andExpect(status().isNotFound());
	}
	@Test
	void check201IfCreatedAnd200IfFound() throws Exception {
		Person person = new Person();
		person.setName("testName");
		person.setAge(10l);
		mvc.perform(post("/person").content(objectMapper.writeValueAsString(person)).contentType("application/json"))
				.andExpect(status().isCreated())
				.andExpect(content().json("{'id': 1}"));
		mvc.perform(get("/person/1"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'name': 'testName', 'age': 10}"));
	}

}
