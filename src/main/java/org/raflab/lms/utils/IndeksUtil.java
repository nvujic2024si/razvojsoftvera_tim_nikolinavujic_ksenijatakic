package org.raflab.lms.utils;

public class IndeksUtil {

    // "  rn   23/2024 " -> "RN 23/2024"
    public static String normalizuj(String indeks) {
        if (indeks == null) {
            return null;
        }
        String n = indeks.trim().replaceAll("\\s+", " ").toUpperCase();
        return n.isEmpty() ? null : n;
    }
}