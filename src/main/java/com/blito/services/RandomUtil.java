package com.blito.services;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomUtil {

    private static final int DEF_COUNT = 20;

    private static final int SMS_ACTIVATE=6;
    
    private static final int EVENT_LINK=5;

    private RandomUtil() {
    }

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
    * Generates a reset key.
    *
    * @return the generated reset key
    */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(SMS_ACTIVATE);
    }
    
    public static String generateLinkRandomNumber()
    {
    	return RandomStringUtils.random(EVENT_LINK);
    }

    /**
     * Generate a SMS Activate code
     * @return
     */

    public static String generateSmsActivateKey() {
        return RandomStringUtils.randomNumeric(SMS_ACTIVATE);
    }
}