package com.usaa.mapper.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.zip.ZipInputStream;

public class IndexerUtil {
    public static final String CLASSIFIER_JAR = "JAR";
    public static final String CLASSIFIER_EAR = "EAR";
    public static final String CLASSIFIER_WAR = "WAR";
    private static final String JAR_FILE_EXTENSION = ".jar";
    private static final String WAR_FILE_EXTENSION = ".war";
    private static final String EAR_FILE_EXTENSION = ".ear";

    public static String getClassifier(String path) {
        if (StringUtils.isNotEmpty(path)) {
            if (path.toLowerCase().endsWith(IndexerUtil.JAR_FILE_EXTENSION)) {
                return CLASSIFIER_JAR;
            } else if (path.toLowerCase().endsWith(IndexerUtil.EAR_FILE_EXTENSION)) {
                return CLASSIFIER_EAR;
            } else if (path.toLowerCase().endsWith(IndexerUtil.WAR_FILE_EXTENSION)) {
                return CLASSIFIER_WAR;
            }
        }
        return "";
    }

    public static String getPackageNameFromFullClassName(String className) {
        StringBuilder returnString = new StringBuilder();

        if (StringUtils.isNotEmpty(className)) {
            String strClass = (className.contains(":")) ? className.substring(className.lastIndexOf(":") + 1) : className;
            String[] tokens = strClass.split("\\.");
            for (int i = 0; i < tokens.length - 1; i++) {
                if (i > 0)
                    returnString.append('.');
                returnString.append(tokens[i]);
            }
        }
        return returnString.toString();
    }

    public static String getClassNameFromFullClassName(String fullClassName) {
        if (StringUtils.isNotEmpty(fullClassName)) {
            String[] tokens = fullClassName.split("\\.");
            return tokens[tokens.length - 1];
        }
        return null;
    }
}
