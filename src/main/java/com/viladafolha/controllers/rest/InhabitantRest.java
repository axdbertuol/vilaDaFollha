package com.viladafolha.controllers.rest;


import com.viladafolha.controllers.service.UserService;
import com.viladafolha.controllers.service.VilaService;
import com.viladafolha.exceptions.InhabitantNotFoundException;

import com.viladafolha.model.transport.InhabitantDTO;
import com.viladafolha.model.transport.MailDTO;
import com.viladafolha.util.JsonResponse;
import com.google.gson.JsonObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    public String getListOfInhabitants() {
        List<InhabitantDTO> inhabitantDTOList = vilaService.list();
        return JsonResponse.returnNamesAndIds(inhabitantDTOList).toString();
    }

    @GetMapping("/{id}")
    public String getInhabitant(@PathVariable(value = "id") String id) throws InhabitantNotFoundException {
        JsonObject response = new JsonObject();
        InhabitantDTO inhabitantDTO = userService.getInhabitant(Long.parseLong(id));
        response.addProperty("http_status", String.valueOf(HttpStatus.OK));
        response.addProperty("name", inhabitantDTO.getName());
        response.addProperty("cpf", inhabitantDTO.getCpf());
        response.addProperty("email", inhabitantDTO.getEmail());
        response.addProperty("password", inhabitantDTO.getPassword());
        response.addProperty("birthday", inhabitantDTO.getBirthday().toString());
        response.addProperty("balance", inhabitantDTO.getBalance());
        response.addProperty("roles", Arrays.toString(inhabitantDTO.getRoles().toArray()));
        return response.toString();
    }

    @GetMapping("/byName={name}")
    public String getInhabitantsByName(@PathVariable(value = "name") String name) {
        List<InhabitantDTO> inhabitantDTOList = userService.getInhabitantByName(name);
        return JsonResponse.returnNamesAndIds(inhabitantDTOList).toString();
    }

    @GetMapping("/byAge={age}")
    public String getInhabitantsByThatAgeOrOlder(@PathVariable(value = "age") String age) {
        List<InhabitantDTO> inhabitantDTOList = userService.getAllByThatAgeOrOlder(age);
        return JsonResponse.returnNamesAndIds(inhabitantDTOList).toString();
    }

    @GetMapping("/byMonth={month}")
    public String getInhabitantsByBirthdayMonth(@PathVariable(value = "month") String month) {
        List<InhabitantDTO> inhabitantDTOList = userService.getInhabitantByBirthdayMonth(month);
        return JsonResponse.returnNamesAndIds(inhabitantDTOList).toString();
    }

    @PostMapping(
            path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String createInhabitant(@RequestBody InhabitantDTO inhab) {

        InhabitantDTO inhabitantDto = userService.createInhabitant(inhab);

        JsonObject response = new JsonObject();
        response.addProperty("http_status", String.valueOf(HttpStatus.ACCEPTED));
        response.addProperty("id", inhabitantDto.getId());
        response.addProperty("name", inhabitantDto.getName());
        response.addProperty("email", inhabitantDto.getEmail());
        return response.toString();
    }

    @DeleteMapping(value = "/remove/{id}")
    public String removeInhabitant(@PathVariable Long id) throws InhabitantNotFoundException {

        JsonObject response = new JsonObject();
        userService.removeInhabitant(id);

        response.addProperty("http_status", String.valueOf(HttpStatus.OK));
        response.addProperty("msg", "Inhabitant with id " + id + " deleted from DB.");

        return response.toString();
    }

    @PostMapping(path = "/generate-new-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String forgot(@RequestBody MailDTO mail) {

        userService.sendNewPassword(mail.getEmail());
        return "New password was sent to your email";
    }

}
