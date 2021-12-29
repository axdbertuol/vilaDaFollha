package com.example.viladafolha.controllers.rest;


import com.example.viladafolha.controllers.service.VilaService;
import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.transport.InhabitantDTO;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNullFields;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/village/inhabitants")
public class InhabitantRest {
    @Autowired
    private VilaService vilaService;

    @GetMapping("/list")
    public List<InhabitantDTO> getListOfInhabitants() throws InhabitantNotFoundException {
        return vilaService.list();
    }

    @GetMapping("/{id}")
    public InhabitantDTO getInhabitant(@PathVariable(value = "id") String id) throws InhabitantNotFoundException {
        return vilaService.getInhabitant(Long.parseLong(id));
    }

    @GetMapping("/byName={name}")
    public List<InhabitantDTO> getInhabitantsByName(@PathVariable(value = "name") String name) {
        return vilaService.getInhabitantByName(name);
    }

    @GetMapping("/byAge={age}")
    public List<InhabitantDTO> getInhabitantsByThatAgeOrOlder(@PathVariable(value = "age") String age) {
        return vilaService.getAllByThatAgeOrOlder(Integer.parseInt(age));
    }

    @GetMapping("/byMonth={month}")
    public List<InhabitantDTO> getInhabitantsByBirthdayMonth(@PathVariable(value = "month") String month) {
        return vilaService.getInhabitantByBirthdayMonth(Integer.parseInt(month));
    }

    @PostMapping(
            path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public JsonObject createInhabitant(@RequestBody InhabitantDTO inhab) {

        Inhabitant inhabitant = vilaService.createInhabitant(inhab);
        JsonObject response = new JsonObject();

        response.addProperty("http_status", String.valueOf(HttpStatus.ACCEPTED));
        response.addProperty("id",inhabitant.getId());
        response.addProperty("name", inhabitant.getName());
        response.addProperty("email", inhabitant.getEmail());
        return response;
    }

}
