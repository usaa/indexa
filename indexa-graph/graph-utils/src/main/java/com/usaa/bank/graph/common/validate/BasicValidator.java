package com.usaa.bank.graph.common.validate;

import org.apache.commons.lang.StringUtils;

/**
 * Utilities for validation purposes for various conditions.
 */
public class BasicValidator {

    /**
     * Check if the object "obj" is NULL or not.
     *
     * @param errorMessage - Error-message to be printed if the object is NULL.
     * @param obj          - Object used for validation.
     * @return - True if not NULL.
     */
    public static boolean validateNotNull(String errorMessage, Object obj) {
        if (obj == null) {
            throw new ValidationException("object is NULL: " + errorMessage);
        }
        return true;
    }

    /**
     * Check the emptiness for the given string "str".
     *
     * @param errorMessage - Error-message to be printed if the object is NULL.
     * @param str          - String used for validation.
     * @return - True if not empty.
     */
    public static boolean validateNotEmpty(String errorMessage, String str) {
        if (StringUtils.isEmpty(str)) {
            throw new ValidationException("str is empty: " + errorMessage);
        }
        return true;
    }

    /**
     * Check the array of objects are not NULL.
     *
     * @param errorMessage - Error-message to be printed if the object is NULL.
     * @param objs         - array of objects used for validation
     * @return - True if array of objects are not NULL.
     */
    public static boolean validateNoNulls(String errorMessage, Object[] objs) {
        BasicValidator.validateNotNull(errorMessage, objs);
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] == null) {
                throw new ValidationException("obj at index:" + i + " is NULL: " + errorMessage);
            }
        }
        return true;
    }

    /**
     * Validate the array of objects are not empty.
     *
     * @param errorMessage - Error message to be printed if the object is NULL.
     * @param objs         - Array of objects used for validation.
     * @return - True if not empty.
     */
    public static boolean validateNotEmpty(String errorMessage, Object[] objs) {
        BasicValidator.validateNotNull(errorMessage, objs);
        if (objs.length == 0) {
            throw new ValidationException("array is empty: " + errorMessage);
        }
        return true;
    }

    /**
     * Validate if the given integer "value" is not negative number.
     *
     * @param errorMessage - Error message to be printed if the object is NULL.
     * @param value        - int value used for validation.
     * @return - True if the value is not negative.
     */
    public static boolean validateNotNegative(String errorMessage, int value) {
        if (value < 0) {
            throw new ValidationException("value is negative: " + errorMessage);
        }
        return true;
    }

    /**
     * Validate if the given double "value" is not negative number.
     *
     * @param errorMessage - Error-message to be printed if the object is NULL.
     * @param value        - double value used for validation.
     * @return - True if the value is not negative.
     */
    public static boolean validateNotNegative(String errorMessage, double value) {
        if (value < 0) {
            throw new ValidationException("value is negative: " + errorMessage);
        }
        return true;
    }

    /**
     * Validate if the given long "value" is not negative number.
     *
     * @param errorMessage - Error message to be printed if the object is NULL.
     * @param value        - long value used for validation.
     * @return - True if the value is not negative.
     */
    public static boolean validateNotNegative(String errorMessage, long value) {
        if (value < 0) {
            throw new ValidationException("value is negative: " + errorMessage);
        }
        return true;
    }


}