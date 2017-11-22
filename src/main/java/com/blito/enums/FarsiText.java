package com.blito.enums;

public enum FarsiText {
    Guest_NAME_IN_PDF("guest.name.in.pdf");

    private final String message;

    FarsiText(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
