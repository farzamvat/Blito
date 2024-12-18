package com.blito.configs;

public final class Constants {
	//Regex for acceptable logins
    public static final String LOGIN_REGEX_OLD = "^[_'.@A-Za-z0-9-]*$";
    public static final String LOGIN_REGEX = "09[0-9]{9}";
    public static final String PHONE_REGEX = "09[0-9]{9}";
    public static final String PHONE_NUMBER_REGEX = "0[0-9]{6,10}";
    public static final String POSTAL_CODE_REGEX = "[0-9]{10}";
    public static final String TEL_REGEX = "[0-9]*";
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
    
    public static final String DEFAULT_HOST_PHOTO = "HOST-PHOTO";
    public static final String DEFAULT_HOST_COVER_PHOTO_1 = "HOST-COVER-PHOTO-1";
    public static final String DEFAULT_HOST_COVER_PHOTO_2 = "HOST-COVER-PHOTO-2";
    public static final String DEFAULT_HOST_COVER_PHOTO_3 = "HOST-COVER-PHOTO-3";
    public static final String DEFAULT_HOST_COVER_PHOTO_4 = "HOST-COVER-PHOTO-4";
    public static final String DEFAULT_EVENT_PHOTO = "EVENT-PHOTO";
    public static final String DEFAULT_EXCHANGEBLIT_PHOTO = "EXCHANGEBLIT_PHOTO";
    
    public static final String FIELD_STRING_TYPE = "string";
    public static final String FIELD_INT_TYPE = "int";
    public static final String FIELD_DOUBLE_TYPE = "double";
    public static final String FIELD_IMAGE_TYPE = "image";

    public static final String BASE_SALON_SCHEMAS = "/salon/schemas";
    public static final String HOST_RESERVED_SEATS = "HOST_RESERVED_SEATS";
    public static final String ACCESS_TOKEN_AUDIENCE = "ACCESS";
    public static final String REFRESH_TOKEN_AUDIENCE = "REFRESH";

    public static final String APARAT_STARTING_URL = "https://www.aparat.com/v/";
    public static final String APARAT_IFRAME_TEMPLATE_PART_1="<style>.h_iframe-aparat_embed_frame{position:relative;} .h_iframe-aparat_embed_frame .ratio {display:block;width:100%;height:auto;} .h_iframe-aparat_embed_frame iframe {position:absolute;top:0;left:0;width:100%; height:100%;}</style><div class=\"h_iframe-aparat_embed_frame\"> <span style=\"display: block;padding-top: 57%\"></span><iframe src=";
    public static final String APARAT_IFRAME_TEMPLATE_PART_2="\"https://www.aparat.com/video/video/embed/videohash/%s/vt/frame\" allowFullScreen=\"true\" webkitallowfullscreen=\"true\" mozallowfullscreen=\"true\" ></iframe></div>";

    public static final long EVENT_DEFAULT_END_DATE = 7258105800000L;
    public static final String EVENT_UPDATE_EDITED_LINK = "EDITED:";

    private Constants()
    {}
}
