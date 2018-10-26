package com.usaa.bank.graph.common.hierarchy;

import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Utility class used for parsing linage list.
 */
public class LineageListParser {
    private static String DEFAULT_DELIMITER = "/";

    /**
     * Parses a string into a LinageList.
     *
     * @param str         - String value that is to be converted to a LinageList.
     * @param delimiter   - Delimiter to be used in LinageList.
     * @param parentFirst - Boolean that represents whether to parse with parent-first ordering or reverse.
     * @return - A LinageList.
     */
    public static LineageList<String> parse(String str, String delimiter, boolean parentFirst) {
        LineageList<String> lList = null;
        if (str != null) {
            lList = new LineageList<String>();
            if (delimiter == null) {
                lList.pushParent(str);
            } else {
                StringTokenizer st = new StringTokenizer(str, delimiter);
                while (st.hasMoreElements()) {
                    if (parentFirst) {
                        lList.queueChild(st.nextToken());
                    } else {
                        lList.pushParent(st.nextToken());
                    }
                }
            }
        }
        return lList;
    }

    /**
     * Create a string where each entry is delimited by the custom delimiter or default delimiter.
     *
     * @param hList       - LinageList.
     * @param delimiter   - Delimiter to be used in LinageList.
     * @param parentsFirst - Boolean that represents whether to parse with parent-first ordering or reverse.
     * @return -  A lineageList represented as string.
     */
    public static String toString(LineageList<? extends Object> hList, String delimiter, boolean parentsFirst) {
        StringBuffer sb = new StringBuffer();
        if (hList != null) {
            if (delimiter == null) {
                delimiter = LineageListParser.DEFAULT_DELIMITER;
            }

            Iterator<? extends Object> it = null;
            if (parentsFirst) {
                it = hList.getParentsFirstIterator();
            } else {
                it = hList.getChildrenFirstIterator();
            }
            if (it != null) {
                if (it.hasNext()) {
                    sb.append(it.next().toString());
                }
                while (it.hasNext()) {
                    sb.append(delimiter + it.next().toString());
                }
            }
        }
        return sb.toString();
    }
}