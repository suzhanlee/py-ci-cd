package example.dev.controller;

import example.dev.entity.Person;
import example.dev.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PersonControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PersonRepository personRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        personRepository.deleteAll(); // Ensure a clean state before each test
    }

    @Test
    public void testIndex() throws Exception {
        Person person1 = new Person();
        person1.setName("John Doe");
        person1.setNumber("123456789");
        personRepository.save(person1);

        Person person2 = new Person();
        person2.setName("Jane Doe");
        person2.setNumber("987654321");
        personRepository.save(person2);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("persons"));

        // Verify the state of the database
        assertThat(personRepository.findAll()).hasSize(2);
    }

    @Test
    public void testAddPerson() throws Exception {
        mockMvc.perform(post("/add")
                        .param("name", "New Person")
                        .param("number", "555555555"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertThat(personRepository.findAll()).hasSize(1);
        Person person = personRepository.findAll().get(0);
        assertThat(person.getName()).isEqualTo("New Person");
        assertThat(person.getNumber()).isEqualTo("555555555");
    }
}
