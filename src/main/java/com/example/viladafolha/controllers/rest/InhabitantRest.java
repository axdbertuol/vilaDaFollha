package com.example.viladafolha.controllers.rest;

import com.example.viladafolha.VilaDaFolhaApplication;
import com.example.viladafolha.controllers.service.VilaService;
import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/village/inhabitants")
public class InhabitantRest {
    @Autowired
    private VilaService vilaService;

    @GetMapping("/list")
    public List<Inhabitant> getListOfInhabitants() throws InhabitantNotFoundException {
        return vilaService.list();
    }


    @GetMapping("/{id}")
    public Inhabitant getInhabitant(@PathVariable(value = "id", name = "id") String id) throws InhabitantNotFoundException {
        return vilaService.getInhabitant(UUID.fromString(id));
    }

    @GetMapping("/byName={name}")
    public List<Inhabitant> getInhabitantByName(@PathVariable(value = "name") String name) throws InhabitantNotFoundException {
        List<Inhabitant> inhabitants = vilaService.getInhabitantByName(name);
        if (inhabitants.isEmpty()) {
            throw new InhabitantNotFoundException();
        }
        return inhabitants;
    }

}
