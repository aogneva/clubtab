package ru.ogneva.clubtab.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.repository.PersonRepository;
import ru.ogneva.clubtab.service.PersonService;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

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

    @AfterEach
    void resetDb() {
        personRepository.deleteAll();
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
            .andExpect(status().isOk())
            .andExpect(content().json(arrStr));
    }

    @Test
    @DisplayName("GET one")
    void getOneOk() throws Exception {
        PersonDTO p = createTestPerson("Елена",  "Максимовна", "Усова",
                new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime());
        mockMvc.perform(MockMvcRequestBuilders.get("/person/{id}", p.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(p.getId()))
            .andExpect(jsonPath("$.firstName").value(p.getFirstName()))
            .andExpect(jsonPath("$.secondName").value(p.getSecondName()))
            .andExpect(jsonPath("$.lastName").value(p.getLastName()))
            .andExpect(jsonPath("$.dob").value((new SimpleDateFormat("DD.MM.YYYY")).format(p.getDob())));
    }

    @Test
    @DisplayName("GET one not found")
    void getOneNotFound() throws Exception {
    }

    @Test
    void create() {
    }

    @Test
    void update() throws Exception {
//        PersonDTO pOld = createTestPerson("Елена",  "Максимовна", "Усова",
//            new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime());
//        PersonDTO p = new PersonDTO(pOld.getId(), pOld.getFirstName(), pOld.getSecondName(), "Швецова",
//                new GregorianCalendar(1987, GregorianCalendar.APRIL, 4).getTime());
//        mockMvc.perform(
//            MockMvcRequestBuilders.put("/person/{id}", pOld.getId())
//                    .content(objectMapper.writeValueAsString(pOld))
//                    .contentType(MediaType.APPLICATION_JSON)
//                    )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(p.getId()))
//                .andExpect(jsonPath("$.firstName").value(p.getFirstName()))
//                .andExpect(jsonPath("$.secondName").value(p.getSecondName()))
//                .andExpect(jsonPath("$.lastName").value(p.getLastName()))
//                .andExpect(jsonPath("$.dob").value((new SimpleDateFormat("DD.MM.YYYY")).format(p.getDob())))
        ;
    }

    private PersonDTO createTestPerson(String firstName, String secondName, String lastName, Date date) {
        return personService.save(new PersonDTO(null, firstName, secondName, lastName, date));
    }
}