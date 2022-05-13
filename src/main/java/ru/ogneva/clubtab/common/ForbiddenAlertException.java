package ru.ogneva.clubtab.common;

import java.io.Serial;

public class ForbiddenAlertException extends Exception {
    private final static String prefix = "Действие запрещено: ";
    @Serial
    private static final long serialVersionUID = -4430200891676634720L;

    public ForbiddenAlertException(String message) {
        super(prefix.concat(message));
    }

}
