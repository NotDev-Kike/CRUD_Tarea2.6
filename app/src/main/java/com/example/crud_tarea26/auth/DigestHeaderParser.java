package com.example.crud_tarea26.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigestHeaderParser {

    private final Map<String, String> map = new HashMap<>();

    public DigestHeaderParser(String header) {
        if (header == null || !header.startsWith("Digest ")) return;

        String digestHeader = header.substring(7);
        parseDigestHeader(digestHeader);
    }

    private void parseDigestHeader(String header) {

        Pattern pattern = Pattern.compile("(\\w+)=(\"[^\"]*\"|[^,\\s]+)");
        Matcher matcher = pattern.matcher(header);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2).replace("\"", ""); // elimina las comillas si las hay
            map.put(key, value);
        }
    }

    public String get(String key) {
        return map.get(key);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public Map<String, String> getAll() {
        return map;
    }
}
