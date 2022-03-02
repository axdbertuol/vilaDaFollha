package com.viladafolha.exceptions;

import java.io.Serial;

public class InhabitantNotFoundException extends Throwable {
    @Serial
    private static final long serialVersionUID = 1L;
    public InhabitantNotFoundException() {
        super("Inhabitant(s) not found.");
    }

}
