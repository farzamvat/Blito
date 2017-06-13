package com.blito.configs;

public final class Constants {
	//Regex for acceptable logins
    public static final String LOGIN_REGEX_OLD = "^[_'.@A-Za-z0-9-]*$";
    public static final String LOGIN_REGEX = "09[0-9]{9}";
    public static final String PHONE_REGEX = "09[0-9]{9}";
    public static final String PHONE_NUMBER_REGEX = "0[0-9]{6,10}";
    public static final String POSTAL_CODE_REGEX = "[0-9]{10}";
    public static final String NATIONAL_ID = "[0-9]{10}";
    public static final String DATE_REGEX = "(13|14)\\d\\d/([1-9]|0[1-9]|1[012])/(0[1-9]|1[0-9]|2[0-9]|3[0-1]|[1-9])";
    public static final String LINK_REGEX = "^(http:\\/\\/|https:\\/\\/)?(wwa-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
    public static final String EMAIL_REGEX = "^[-!#$%&'*+/0-9=?A-Z^_a-z{|}~](\\.?[-!#$%&'*+/0-9=?A-Z^_a-z{|}~])*@[a-zA-Z](-?[a-zA-Z0-9])*(\\.[a-zA-Z](-?[a-zA-Z0-9])*)+$";

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 60;

    // Spring profiles for development, test and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";
    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_SWAGGER = "swagger";


    public static final String SYSTEM_ACCOUNT = "system";
    public static final String NOTIF_COMMUNICATION_VERSION = "1.0.0";
    public static final int MAX_ACTIVE_SMS_RETRY = 5;
    public static final int MAX_INCORRECT_PASSWORD_BEFORE_BAN = 3;
    
    public static final String DEFAULT_EVENT_BANNER = "EVENT-BANNER";
    public static final String DEFAULT_HOST_PHOTO = "HOST-PHOTO";
    public static final String DEFAULT_HOST_COVER_PHOTO = "HOST-COVER-PHOTO";
    public static final String DEFAULT_EVENT_PHOTO = "EVENT-PHOTO";
    public static final String DEFAULT_EXCHANGEBLIT_PHOTO = "EXCHANGEBLIT_PHOTO";

    private Constants()
    {}
}
