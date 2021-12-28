package com.example.viladafolha.exceptions;

import java.io.Serial;
import java.util.function.Supplier;

public class InhabitantNotFoundException extends Throwable {
    @Serial
    private static final long serialVersionUID = 1L;
    public InhabitantNotFoundException() {
        super("Inhabitant(s) not found.");
    }

}
