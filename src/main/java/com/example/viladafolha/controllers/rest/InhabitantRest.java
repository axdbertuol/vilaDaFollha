package com.example.viladafolha.controllers.rest;


import com.example.viladafolha.controllers.service.UserService;
import com.example.viladafolha.controllers.service.VilaService;
import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.transport.CredentialsDTO;
import com.example.viladafolha.model.transport.InhabitantDTO;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/village/inhabitants")
public class InhabitantRest {

    private final VilaService vilaService;
    private final UserService userService;

    public InhabitantRest(VilaService vilaService, UserService userService) {
        this.vilaService = vilaService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public List<InhabitantDTO> getListOfInhabitants() {
        return vilaService.list();
    }

    @GetMapping("/{id}")
    public InhabitantDTO getInhabitant(@PathVariable(value = "id") String id) throws InhabitantNotFoundException {
        return userService.getInhabitant(Long.parseLong(id));
    }

    @GetMapping("/byName={name}")
    public List<InhabitantDTO> getInhabitantsByName(@PathVariable(value = "name") String name) {
        return userService.getInhabitantByName(name);
    }

    @GetMapping("/byAge={age}")
    public List<InhabitantDTO> getInhabitantsByThatAgeOrOlder(@PathVariable(value = "age") String age) {
        return userService.getAllByThatAgeOrOlder(Integer.parseInt(age));
    }

    @GetMapping("/byMonth={month}")
    public List<InhabitantDTO> getInhabitantsByBirthdayMonth(@PathVariable(value = "month") String month) {
        return userService.getInhabitantByBirthdayMonth(Integer.parseInt(month));
    }

    @PostMapping(
            path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String createInhabitant(@RequestBody InhabitantDTO inhab) {

        JsonObject response = new JsonObject();
        //TODO: check password validity

        Inhabitant inhabitant = userService.createInhabitant(inhab);

        response.addProperty("http_status", String.valueOf(HttpStatus.ACCEPTED));
        response.addProperty("id", inhabitant.getId());
        response.addProperty("name", inhabitant.getName());
        response.addProperty("email", inhabitant.getEmail());
        return response.toString();
    }

    @PostMapping(
            path = "/remove",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String removeInhabitant(@RequestBody CredentialsDTO credentialsDTO) {

        JsonObject response = new JsonObject();
        InhabitantDTO inhabitant = userService.removeInhabitant(credentialsDTO.getEmail());

        response.addProperty("http_status", String.valueOf(HttpStatus.OK));
        response.addProperty("id", inhabitant.getId());
        response.addProperty("name", inhabitant.getName());
        response.addProperty("email", inhabitant.getEmail());
        response.addProperty("msg", "Inhabitant deleted from DB.");
        return response.toString();
    }

}
