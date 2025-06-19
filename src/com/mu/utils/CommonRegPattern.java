package com.mu.utils;

import java.util.regex.Pattern;

public class CommonRegPattern {
   public static final Pattern PATTERN_MSG_PARAM = Pattern.compile("%n%");
   public static final Pattern PATTERN_INT = Pattern.compile("[-]?\\d+");
   public static final Pattern PATTERN_TASK_RATE = Pattern.compile("\\{\\s*s\\s*(\\d+)\\s*([cmi]?)\\s*}");
   public static final Pattern PATTERN_TASK_PROFESSION = Pattern.compile("\\{\\s*z\\s*(\\d+)#\\s*(.*?)\\s*#\\s*}");
   public static final Pattern PATTERN_PROJECTION_VALUE = Pattern.compile("\\{\\s*(\\d+)\\s*}");
}
