package ru.at.ui.data;

import java.util.UUID;

public final class TestData {

    public static final String LOGIN_USERNAME = "practice";
    public static final String LOGIN_PASSWORD = "SuperSecretPassword!";
    public static final String INVALID_LOGIN_PASSWORD = "WrongPassword";
    public static final String REGISTRATION_PASSWORD = "StrongPassword123!";

    public static final String UPLOAD_FIXTURE = "upload/diploma-upload.txt";
    public static final String UPLOAD_FILE_NAME = "diploma-upload.txt";

    private static final String REGISTRATION_USERNAME_PREFIX = "diploma";
    private static final int REGISTRATION_USERNAME_RANDOM_LENGTH = 10;

    private TestData() {
    }

    public static String randomRegistrationUsername() {
        return REGISTRATION_USERNAME_PREFIX
                + UUID.randomUUID().toString().replace("-", "").substring(0, REGISTRATION_USERNAME_RANDOM_LENGTH);
    }
}
