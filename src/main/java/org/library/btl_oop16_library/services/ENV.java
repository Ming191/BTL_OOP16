package org.library.btl_oop16_library.services;

import io.github.cdimascio.dotenv.Dotenv;

public class ENV {
    private static Dotenv instance;

    private ENV() {

    }

    public static Dotenv getInstance() {
        if (instance == null) {
            instance = Dotenv.configure()
                    .directory(".")
                    .filename(".env")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
        }
        return instance;
    }

    public String get(String key) {
        return instance.get(key);
    }
}
