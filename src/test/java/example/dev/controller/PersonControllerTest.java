package example.dev.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import example.dev.entity.Person;
import example.dev.service.PersonService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testIndex() throws Exception {
        Person person1 = new Person();
        person1.setName("John Doe");
        person1.setNumber("123456789");

        Person person2 = new Person();
        person2.setName("Jane Doe");
        person2.setNumber("987654321");

        given(personService.getAllPersons()).willReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("persons"));
    }

    @Test
    public void testAddPerson() throws Exception {
        String name = "New Person";
        String number = "555555555";

        mockMvc.perform(post("/add")
                        .param("name", name)
                        .param("number", number))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(personService).savePerson(new Person(name, number));
    }
}
