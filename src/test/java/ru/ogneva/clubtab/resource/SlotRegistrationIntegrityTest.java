package ru.ogneva.clubtab.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.ogneva.clubtab.common.Constants;
import ru.ogneva.clubtab.domain.*;
import ru.ogneva.clubtab.dto.SlotRegistrationDTO;
import ru.ogneva.clubtab.repository.*;
import ru.ogneva.clubtab.service.SlotRegistrationService;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Slot Registration Integrity Tests")
class SlotRegistrationIntegrityTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private StateTypeRepository stateTypeRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SlotRegistrationService slotRegistrationService;

    @Autowired
    private SlotRegistrationRepository slotRegistrationRepository;

    @Autowired
    private MockMvc mockMvc;

    final private List<Long> personToDeleteList = new ArrayList<>();

    final private List<Long> slotToDeleteList = new ArrayList<>();

    final private List<Long> slotRegistrationToDeleteList = new ArrayList<>();
    private PersonEntity masterYoga;
    private PersonEntity massager;

    private SlotEntity jogaSlot;

    private SlotEntity massageSlot;

    @BeforeEach
    private void before() {
        StateTypeEntity state = stateTypeRepository.findByTag(Constants.StateTypes.STATE_SCHEDULED).orElseThrow();
        ServiceTypeEntity serviceYoga = serviceTypeRepository.findByTag(Constants.ServiceTypes.GROUP_HATHA_I).orElseThrow();
        ServiceTypeEntity serviceMassage = serviceTypeRepository.findByTag(Constants.ServiceTypes.BODY_MASSAGE).orElseThrow();
        masterYoga = new PersonEntity(null, "Елена",  "Максимовна", "Усова", "9124578274", null);
        massager = new PersonEntity(null, "Наталья",  "Максимовна", "Усова", "9124578275", null);
        personRepository.save(masterYoga);
        personRepository.save(massager);
        personToDeleteList.add(masterYoga.getId());
        personToDeleteList.add(massager.getId());
        jogaSlot = new SlotEntity(null, Instant.now(), 60L, serviceMassage.getCapacity(), serviceYoga, masterYoga, state, null);
        massageSlot = new SlotEntity(null, Instant.now(), 45L, serviceMassage.getCapacity(), serviceMassage, massager, state, null);
        jogaSlot = slotRepository.save(jogaSlot);
        massageSlot = slotRepository.save(massageSlot);
        slotToDeleteList.add(jogaSlot.getId());
        slotToDeleteList.add(massageSlot.getId());

    }

    @AfterEach
    private void after() {
        slotRegistrationToDeleteList.forEach(slotRegId -> slotRegistrationRepository.deleteById(slotRegId));
        slotRegistrationToDeleteList.clear();
        slotToDeleteList.forEach(slotId -> slotRepository.deleteById(slotId));
        slotToDeleteList.clear();
        personToDeleteList.forEach(slotId -> personRepository.deleteById(slotId));
        personToDeleteList.clear();
    }

    @Test
    @DisplayName("GET all slot registrations")
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/slot-reg"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET slot registrations by customer")
    void getSlotRegByCustomer() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/slot-reg/customer/{customerId}", masterYoga.getId()))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.customerId").value(masterYoga.getId())
//        );
    }

    @Test
    @DisplayName("GET slot registrations by slot")
    void getSlotRegBySlot() {
    }

    @Test
    @DisplayName("GET slot registration by id")
    void getOne() throws Exception {
//        Long slotReg = 1L;
//        mockMvc.perform(MockMvcRequestBuilders.get("/slot-reg/{id}", slotReg))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id").value(slotReg)
//        );
    }

    @Test
    @DisplayName("POST slot registration")
    void create() {
        SlotRegistrationDTO reg = slotRegistrationService.create(massageSlot.getId(), masterYoga.getId());
        slotRegistrationToDeleteList.add(reg.getId());
        List<SlotRegistrationEntity> slotRegs = slotRegistrationRepository.findAll();
        assertThat(slotRegs, is(not(empty())));
    }

    @Test
    @DisplayName("DELETE slot registration")
    void delete() {
    }


}