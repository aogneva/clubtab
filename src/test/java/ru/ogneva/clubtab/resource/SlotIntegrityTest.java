package ru.ogneva.clubtab.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.ogneva.clubtab.common.Constants;
import ru.ogneva.clubtab.domain.PersonEntity;
import ru.ogneva.clubtab.domain.ServiceTypeEntity;
import ru.ogneva.clubtab.domain.SlotEntity;
import ru.ogneva.clubtab.domain.StateTypeEntity;
import ru.ogneva.clubtab.dto.SlotDTO;
import ru.ogneva.clubtab.repository.PersonRepository;
import ru.ogneva.clubtab.repository.ServiceTypeRepository;
import ru.ogneva.clubtab.repository.SlotRepository;
import ru.ogneva.clubtab.repository.StateTypeRepository;
import ru.ogneva.clubtab.service.SlotService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Slot Integrity Tests")
class SlotIntegrityTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotService slotService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private StateTypeRepository stateTypeRepository;

    @Autowired
    private MockMvc mockMvc;

    static private PersonEntity executor;

    final private List<Long> toDeleteList = new ArrayList<>();

    @BeforeEach
    private void before() {
        PersonEntity p = new PersonEntity(null, "Елена",  "Максимовна", "Усова", "9124578274");
        executor = personRepository.save(p);
    }

    @AfterEach
    private void after() {
        toDeleteList.forEach(slotId -> slotService.delete(slotId));
        toDeleteList.clear();
        personRepository.deleteById(executor.getId());
    }

    @Test
    @DisplayName("GET all slots")
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/slot"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET slot")
    void getOne() throws Exception {
        ServiceTypeEntity serviceType = serviceTypeRepository.findByTag(Constants.ServiceTypes.BODY_MASSAGE).orElseThrow();
        StateTypeEntity state = stateTypeRepository.findByTag(Constants.StateTypes.STATE_SCHEDULED).orElseThrow();
        SlotEntity dto = new SlotEntity(null, Instant.now(), 30L, serviceType, executor, state);
        dto = slotRepository.save(dto);
        if (dto.getId()!=null) {
            toDeleteList.add(dto.getId());
        }

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/slot/{id}", dto.getId())
            )
            .andExpectAll(status().isOk(),
                jsonPath("$.duration").value(dto.getDuration()),
                jsonPath("$.stateId").value(state.getId()),
                jsonPath("$.serviceTypeId").value(dto.getServiceType().getId()),
                jsonPath("$.executorId").value(dto.getExecutor().getId())
            )
            .andReturn();
    }

    @Test
    @DisplayName("POST & PUT slot")
    void create() throws Exception {
        StateTypeEntity state = stateTypeRepository.findByTag(Constants.StateTypes.STATE_SCHEDULED).orElseThrow();
        ServiceTypeEntity serviceType = serviceTypeRepository.findByTag(Constants.ServiceTypes.BODY_PILLING).orElseThrow();
        SlotDTO dto = SlotDTO.builder()
                .id(null)
                .duration(null) /* !! check duration setting default */
                .startTime(new GregorianCalendar(2022,GregorianCalendar.MAY,12).toInstant())
                .stateId(state.getId())
                .executorId(executor!=null ? executor.getId() : null)
                .serviceTypeId(serviceType.getId())
                .build();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/slot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
            )
            .andExpectAll(status().isCreated(),
                jsonPath("$.id").isNotEmpty(),
                jsonPath("$.duration").value(serviceType.getDuration()), /* !! check duration setting default */
                jsonPath("$.stateId").value(dto.getStateId()),
                jsonPath("$.serviceTypeId").value(dto.getServiceTypeId()),
                jsonPath("$.executorId").value(dto.getExecutorId())
            )
            .andReturn();
        Long id = JsonPath.parse(response.getResponse().getContentAsString()).read("$.id");
        assertThat(id, is(notNullValue()));
        toDeleteList.add(id.longValue());
        //---------------------
        SlotDTO updateDto = dto.clone();
        ServiceTypeEntity newServiceType = serviceTypeRepository.findByTag(Constants.ServiceTypes.BODY_PILLING).orElseThrow();
        StateTypeEntity newState = stateTypeRepository.findByTag(Constants.StateTypes.STATE_COMPLETED).orElseThrow();
        updateDto.setId(id.longValue());
        updateDto.setDuration(90L);
        updateDto.setExecutorId(executor.getId());
        updateDto.setStartTime(dto.getStartTime().minusSeconds(30L));
        updateDto.setServiceTypeId(newServiceType.getId());
        updateDto.setStateId(newState.getId());
        mockMvc.perform(MockMvcRequestBuilders.put("/slot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(updateDto.getId()),
                        jsonPath("$.duration").value(updateDto.getDuration()),
                        jsonPath("$.stateId").value(updateDto.getStateId()),
                        jsonPath("$.serviceTypeId").value(updateDto.getServiceTypeId()),
                        jsonPath("$.executorId").value(updateDto.getExecutorId())
                );
    }

    @Test
    @Disabled
    @DisplayName("PUT slot")
    void updateSlot() throws Exception {
        ServiceTypeEntity serviceType = serviceTypeRepository.findByTag(Constants.ServiceTypes.BODY_MASSAGE).orElseThrow();
        StateTypeEntity state = stateTypeRepository.findByTag(Constants.StateTypes.STATE_SCHEDULED).orElseThrow();
        SlotDTO dto = SlotDTO.builder()
                .id(null)
                .duration(null)
                .startTime(new GregorianCalendar(2022,GregorianCalendar.MAY,12).toInstant())
                .stateId(state.getId())
                .executorId(null)
                .serviceTypeId(serviceType.getId())
                .build();
        SlotDTO updateDto = slotService.create(dto);
        if (updateDto!=null) {
            toDeleteList.add(updateDto.getId());
        }
        ServiceTypeEntity newServiceType = serviceTypeRepository.findByTag(Constants.ServiceTypes.BODY_PILLING).orElseThrow();
        StateTypeEntity newState = stateTypeRepository.findByTag(Constants.StateTypes.STATE_COMPLETED).orElseThrow();
        updateDto.setDuration(90L);
        updateDto.setExecutorId(executor.getId());
        updateDto.setStartTime(dto.getStartTime().minusSeconds(30L));
        updateDto.setServiceTypeId(newServiceType.getId());
        updateDto.setStateId(newState.getId());

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/slot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(updateDto.getId()),
                        jsonPath("$.duration").value(updateDto.getDuration()),
                        jsonPath("$.stateId").value(updateDto.getStateId()),
                        jsonPath("$.serviceTypeId").value(updateDto.getServiceTypeId()),
                        jsonPath("$.executorId").value(updateDto.getExecutorId())
                )
                .andReturn();
        Integer id = JsonPath.parse(response.getResponse().getContentAsString()).read("$.id");
        if (id!=null) {
            toDeleteList.add(id.longValue());
        }

    }


}