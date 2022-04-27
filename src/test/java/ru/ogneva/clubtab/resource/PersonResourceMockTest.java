package ru.ogneva.clubtab.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.service.PersonService;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PersonResource.class)
class PersonResourceMockTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    PersonService personService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("GET all persons")
    void getAll() throws Exception {
        List<PersonDTO> personList = new ArrayList<>();
        personList.add( new PersonDTO(1L, "Татьяна", "Степановна", "Даничкина",
                new GregorianCalendar(1987, GregorianCalendar.APRIL, 14).getTime()));
        personList.add( new PersonDTO(2L, "Светлана", "Васильевна", "Галкина",
                new GregorianCalendar(1974, GregorianCalendar.SEPTEMBER, 7).getTime()));
        personList.add( new PersonDTO(3L, "Александр", "Николаевич", "Веселов",
                new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime()));
        Mockito.when(personService.getAll()).thenReturn(personList);
        mvc.perform(MockMvcRequestBuilders.get("/person/all"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(personList)));
    }

    @Test
    @DisplayName("GET one person")
    void getOne() throws Exception {
        PersonDTO personDTO = new PersonDTO(3L, "Александр", "Николаевич", "Веселов",
                new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime());
        Mockito.when(personService.getOne(Mockito.anyLong())).thenReturn(Optional.of(personDTO));
        mvc.perform(MockMvcRequestBuilders.get("/person/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.firstName").value("Александр"))
                .andExpect(jsonPath("$.secondName").value("Николаевич"))
                .andExpect(jsonPath("$.lastName").value("Веселов"));
    }

    @Test
    @DisplayName("POST create person")
    void create() throws Exception {
        PersonDTO person = new PersonDTO(2L, "Светлана", "Васильевна", "Галкина",
                new GregorianCalendar(1974, GregorianCalendar.SEPTEMBER, 7).getTime());
        Mockito.when(personService.save(
                Mockito.isNull(),Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(person);
        mvc.perform(MockMvcRequestBuilders.post("/person/new")
                        .param("firstName", "Светлана")
                        .param("secondName", "Васильевна")
                        .param("lastName", "Галкина")
                        .param("dob", "07.09.1974")
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PUT update person")
    void update() throws Exception {
        PersonDTO person = new PersonDTO(2L, "Светлана", "Васильевна", "Галкина",
                new GregorianCalendar(1974, GregorianCalendar.SEPTEMBER, 7).getTime());
        Mockito.when(personService.save(Mockito.anyLong(),
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(person);
        mvc.perform(MockMvcRequestBuilders.put("/person/update")
                        .content(objectMapper.writeValueAsString(person))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value("Светлана"))
                .andExpect(jsonPath("$.secondName").value("Васильевна"))
                .andExpect(jsonPath("$.lastName").value("Галкина"));
    }

}