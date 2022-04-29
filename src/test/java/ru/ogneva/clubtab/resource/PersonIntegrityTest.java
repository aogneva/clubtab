package ru.ogneva.clubtab.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONAware;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.repository.PersonRepository;
import ru.ogneva.clubtab.service.PersonService;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Person Integrity Tests")
class PersonIntegrityTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    private final List<Long> toDeleteList = new ArrayList<>();

    @AfterEach
    void resetDb() {
        toDeleteList.forEach(personId -> personRepository.deleteById(personId));
        toDeleteList.clear();
    }

    @Test
    @DisplayName("GET all")
    void getAll() throws Exception {
        PersonDTO p1 = createTestPerson("Игорь", "Николаевич", "Титов",
                new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime());
        PersonDTO p2 = createTestPerson("Елена",  "Максимовна", "Усова",
                new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime());
        String arrStr = objectMapper.writeValueAsString(Arrays.asList(p1, p2));
        mockMvc.perform(MockMvcRequestBuilders.get("/person"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET person")
    void getOneOk() throws Exception {
        PersonDTO p = createTestPerson("Елена",  "Максимовна", "Усова",
                new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime());
        mockMvc.perform(MockMvcRequestBuilders.get("/person/{id}", p.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(p.getId()))
            .andExpect(jsonPath("$.firstName").value(p.getFirstName()))
            .andExpect(jsonPath("$.secondName").value(p.getSecondName()))
            .andExpect(jsonPath("$.lastName").value(p.getLastName()))
        ;
    }

    @Test
    @DisplayName("GET person not found")
    void getOneNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST person")
    void createTest() throws Exception {
        GregorianCalendar gregorianDateTime = new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3);
        gregorianDateTime.setTimeZone(TimeZone.getTimeZone("UTC"));
        PersonDTO person = new PersonDTO( null, "Елена",  "Максимовна", "Усова",
                gregorianDateTime.getTime());
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
            )
            .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.secondName").value(person.getSecondName()))
                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                .andReturn();
        Integer id = JsonPath.parse(response.getResponse().getContentAsString()).read("$.id");
        if (id!=null) {
            toDeleteList.add(id.longValue());
        }
    }

    @Test
    @DisplayName("PUT person")
    void update() throws Exception {
        PersonDTO pOld = createTestPerson("Елена",  "Максимовна", "Усова",
            new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime());
        PersonDTO p = new PersonDTO(pOld.getId(), pOld.getFirstName(), pOld.getSecondName(), "Швецова", pOld.getDob());
        mockMvc.perform(
            MockMvcRequestBuilders.put("/person/{id}", p.getId())
                    .content(objectMapper.writeValueAsString(p))
                    .contentType(MediaType.APPLICATION_JSON)
                    )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(p.getId()))
                .andExpect(jsonPath("$.firstName").value(p.getFirstName()))
                .andExpect(jsonPath("$.secondName").value(p.getSecondName()))
                .andExpect(jsonPath("$.lastName").value(p.getLastName()));
    }

    @Test
    @DisplayName("PUT person not found")
    void updateNotFound() throws Exception {
        PersonDTO person = new PersonDTO(1L, "Елена",  "Максимовна", "Усова",
                new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime());
        mockMvc.perform(MockMvcRequestBuilders.put("/person/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
            .andExpect(status().isNotFound());
    }

    private PersonDTO createTestPerson(String firstName, String secondName, String lastName, Date date) {
        PersonDTO person = personService.save(new PersonDTO(null, firstName, secondName, lastName, date));
        toDeleteList.add(person.getId());
        return person;
    }
}