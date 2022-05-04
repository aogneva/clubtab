package ru.ogneva.clubtab.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ogneva.clubtab.dto.StateTypeDTO;
import ru.ogneva.clubtab.service.StateTypeService;

import java.util.List;

@RestController
@RequestMapping(value = "/state-type")
public class StateTypeResource {
    final private StateTypeService stateTypeService;

    StateTypeResource(StateTypeService stateTypeService) {
        this.stateTypeService = stateTypeService;
    }

    @GetMapping
    public List<StateTypeDTO> getAll() {
        return stateTypeService.getAll();
    }

    @GetMapping(value="/{id}")
    public StateTypeDTO get(@PathVariable("id") Long id) {
        return stateTypeService.get(id).orElse(null);
    }

}
