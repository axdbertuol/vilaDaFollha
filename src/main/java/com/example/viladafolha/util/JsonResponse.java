package com.example.viladafolha.util;

import com.example.viladafolha.model.transport.InhabitantDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class JsonResponse {

    public static JsonArray returnNamesAndIds(List<InhabitantDTO> inhabitantDTOList) {

        JsonArray response = new JsonArray();

        for (InhabitantDTO inhab : inhabitantDTOList) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", inhab.getId());
            obj.addProperty("name", inhab.getName());
            response.add(obj);
        }
        return response;
    }
}
