package com.atixlabs.semillasmiddleware.util;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {
    public static String cleanString(String category) {
        return StringUtils.stripAccents(category.toUpperCase().replaceAll("[0123456789]", "").trim());
    }
}
