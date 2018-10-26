package com.usaa.mapper.util;

import com.usaa.bank.asset.index.api.domain.asset.gav.*;
import org.apache.commons.lang.StringUtils;

import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ArtifactInspector {
    public static GAV getArtifactGAV(String virtualLocation, Version defaultVersion) {
        try {
            GroupId groupId = new GroupId(parseGroupIdOffPath(virtualLocation));
            String strVersion = parseVersionNrOffPath(virtualLocation);
            Version version = (defaultVersion != null) ? defaultVersion : new Version(strVersion);

            String strArtifact = parseArtifactIdOffPath(virtualLocation);
            if (strArtifact.lastIndexOf(".") > -1) {
                strArtifact = strArtifact.substring(0, strArtifact.lastIndexOf("."));
            }
            strArtifact = strArtifact.replaceAll("-" + strVersion, "");

            ArtifactId artifactId = new ArtifactId(strArtifact);
            GAV gav = new GAV(groupId, artifactId, version, ArtifactType.COMPONENT);

            return gav;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseVersionNrOffPath(String virtualLocation) {
        String[] virtualLocationArray = virtualLocation.split("/");
        String fileName = virtualLocationArray[virtualLocationArray.length - 1];
        Pattern p = Pattern.compile("([0-9]+[\\.\\-\\_][0-9.-_]+)\\.[jwe]ar$");
        Matcher match = p.matcher(fileName);
        if (match.find()) {
            return match.group(1);
        }
        return "unknown";
    }

    public static String parseArtifactIdOffPath(Path path) {
        return parseArtifactIdOffPath(path.toAbsolutePath().toString());
    }

    public static String parseArtifactIdOffPath(String virtualLocation) {
        if (StringUtils.isNotEmpty(virtualLocation)) {
            String[] virtualLocationArray = virtualLocation.split("!");
            if (virtualLocationArray.length > 1) {
                return virtualLocationArray[virtualLocationArray.length - 1];
            } else {
                String[] str = virtualLocationArray[0].split("\\\\");
                return str[str.length - 1];
            }
        }
        return "";
    }

    public static String parseGroupIdOffPath(String virtualDirectory) {
        String returnString = "";
        if (StringUtils.isNotEmpty(virtualDirectory)) {
            int archiveDelimIndex = virtualDirectory.indexOf("!");
            if (archiveDelimIndex != -1) {
                virtualDirectory = virtualDirectory.substring(0, archiveDelimIndex - 1);
            }

            virtualDirectory = virtualDirectory.replaceAll("\\\\", "/");
            if (virtualDirectory.startsWith("/")) {
                virtualDirectory = virtualDirectory.substring(1);
            }
            List<String> virtualDirectoryList = new ArrayList(Arrays.asList(virtualDirectory.split("/")));
            virtualDirectoryList.remove(0);
            virtualDirectoryList.remove(virtualDirectoryList.size() - 1);
            int directoryMax = virtualDirectoryList.size();
            for (int i = virtualDirectoryList.size() - 1; i >= 0; i--) {
                if (virtualDirectoryList.get(i).equals("config") || virtualDirectoryList.get(i).equals("lib") || virtualDirectoryList.get(i).equals("scripts")) {
                    directoryMax = i;
                    break;
                }
            }
            for (int i = virtualDirectoryList.size() - 1; i >= directoryMax; i--) {
                virtualDirectoryList.remove(i);
            }

            returnString = StringUtils.join(virtualDirectoryList, ".");

        }
        return returnString;
    }
}
