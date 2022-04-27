package ru.ogneva.clubtab.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.service.PersonService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PersonIntegrityTest {

    @Autowired
    PersonService personService;

    @Autowired
    private PersonResource personResource;

    @BeforeEach
    void setUp() {
//        personList = new ArrayList<>();
//        personList.add(
//            new PersonDTO(1L, "Татьяна", "Степановна", "Даничкина",
//                new GregorianCalendar(1987, GregorianCalendar.APRIL, 14).getTime()));
//        personList.add(
//            new PersonDTO(2L, "Светлана", "Васильевна", "Ганичкина",
//                new GregorianCalendar(1974, GregorianCalendar.SEPTEMBER, 7).getTime()));
//        personList.add(
//            new PersonDTO(3L, "Александр", "Николаевич", "Веселов",
//                new GregorianCalendar(1990, GregorianCalendar.JANUARY, 3).getTime()));
    }

    @Test
    @DisplayName("GET all")
    void getAll() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/person/all"))
//                .andExpect(status().isOk())
//                .andExpect(result -> {
//                    result.getResponse().getContentType().equalsIgnoreCase("List");
//                });
        List<PersonDTO> list = personResource.getAll();
        assertTrue(list.size() > 0);
    }

    @Test
    void getOne() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

}