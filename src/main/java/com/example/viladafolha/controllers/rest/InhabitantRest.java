package com.example.viladafolha.controllers.rest;

import com.example.viladafolha.VilaDaFolhaApplication;
import com.example.viladafolha.controllers.service.VilaService;
import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.transport.InhabitantDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
    public InhabitantDTO getInhabitant(@PathVariable(value = "id", name = "id") String id) throws InhabitantNotFoundException {
        return vilaService.getInhabitant(Long.parseLong(id));
    }

    @GetMapping("/byName={name}")
    public List<InhabitantDTO> getInhabitantByName(@PathVariable(value = "name") String name) throws InhabitantNotFoundException {
        List<InhabitantDTO> inhabitants = vilaService.getInhabitantByName(name);
        if (inhabitants.isEmpty()) {
            throw new InhabitantNotFoundException();
        }
        return inhabitants;
    }

}
