package ru.ogneva.clubtab.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.dto.SlotDTO;
import ru.ogneva.clubtab.service.PersonService;
import ru.ogneva.clubtab.service.SlotService;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Slot Integrity Tests")
class SlotIntegrityTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SlotService slotService;

    @Autowired
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    static private PersonDTO executor;

    final private List<Long> toDeleteList = new ArrayList<>();

    @BeforeEach
    private void before() {
        PersonDTO p = new PersonDTO(null, "Елена",  "Максимовна", "Усова", "9124578274");
        executor = personService.save(p);
    }

    @AfterEach
    private void after() {
        toDeleteList.forEach(slotId -> slotService.delete(slotId));
        toDeleteList.clear();
        personService.delete(executor.getId());
    }

    @Test
    @DisplayName("GET all")
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/slot"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST create slot")
    void create() throws Exception {
        SlotDTO dto = createTestDto();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/slot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
            )
            .andExpectAll(status().isCreated(),
                jsonPath("$.duration").value(dto.getDuration()),
                jsonPath("$.stateId").value(dto.getStateId()),
                jsonPath("$.serviceTypeId").value(dto.getServiceTypeId()),
                jsonPath("$.executorId").value(dto.getExecutorId())
            )
            .andReturn();
        Integer id = JsonPath.parse(response.getResponse().getContentAsString()).read("$.id");
        if (id!=null) {
            toDeleteList.add(id.longValue());
        }

    }

    private SlotDTO createTestDto() {
        return SlotDTO.builder()
                .id(null)
                .duration(60L)
                .startTime(
                        new GregorianCalendar(
                                2022,
                                GregorianCalendar.MAY,
                                12).toInstant()
                )
                .stateId(1L)
                .executorId(executor!=null ? executor.getId() : null)
                .serviceTypeId(5L)
                .build();
    }
}