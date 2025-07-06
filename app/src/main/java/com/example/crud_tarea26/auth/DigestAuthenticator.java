package com.example.crud_tarea26.auth;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class DigestAuthenticator implements Authenticator {

    private final String username;
    private final String password;

    private int nonceCount = 0;

    public DigestAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        String wwwAuth = response.header("WWW-Authenticate");
        System.out.println("WWW-Authenticate header: " + wwwAuth);

        if (wwwAuth == null || !wwwAuth.startsWith("Digest ")) {
            return null;
        }

        Map<String, String> authParams = parseDigestHeader(wwwAuth);

        String realm = authParams.get("realm");
        String nonce = authParams.get("nonce");
        String qop = authParams.get("qop");
        String opaque = authParams.get("opaque");
        String algorithm = authParams.get("algorithm");

        if (algorithm == null || algorithm.isEmpty()) {
            algorithm = "MD5";
        }

        String method = response.request().method();
        String uri = response.request().url().encodedPath();

        nonceCount++;
        String nc = String.format("%08x", nonceCount);
        String cnonce = UUID.randomUUID().toString().replace("-", "");

        String HA1 = md5(username + ":" + realm + ":" + password);
        String HA2 = md5(method + ":" + uri);

        String responseDigest;
        if (qop != null && qop.contains("auth")) {
            responseDigest = md5(HA1 + ":" + nonce + ":" + nc + ":" + cnonce + ":auth:" + HA2);
        } else {
            responseDigest = md5(HA1 + ":" + nonce + ":" + HA2);
        }

        StringBuilder authorization = new StringBuilder();
        authorization.append("Digest ");
        authorization.append("username=\"").append(username).append("\", ");
        authorization.append("realm=\"").append(realm).append("\", ");
        authorization.append("nonce=\"").append(nonce).append("\", ");
        authorization.append("uri=\"").append(uri).append("\", ");
        authorization.append("response=\"").append(responseDigest).append("\"");

        if (opaque != null && !opaque.isEmpty()) {
            authorization.append(", opaque=\"").append(opaque).append("\"");
        }

        if (qop != null && qop.contains("auth")) {
            authorization.append(", qop=auth, nc=").append(nc).append(", cnonce=\"").append(cnonce).append("\"");
        }

        authorization.append(", algorithm=").append(algorithm);

        return response.request().newBuilder()
                .header("Authorization", authorization.toString())
                .build();
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes("ISO-8859-1")); // encoding recomendado para Digest
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, String> parseDigestHeader(String header) {
        Map<String, String> map = new HashMap<>();
        header = header.substring("Digest ".length());

        String[] tokens = header.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String token : tokens) {
            String[] keyVal = token.trim().split("=", 2);
            if (keyVal.length == 2) {
                String key = keyVal[0].trim();
                String val = keyVal[1].trim().replaceAll("^\"|\"$", "");
                map.put(key, val);
            }
        }
        return map;
    }
}
