package dev.kiddo.autohappybirthday.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import dev.kiddo.autohappybirthday.birthday.BirthdayList;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static dev.kiddo.autohappybirthday.client.AutoHappyBirthdayClient.*;

public class BirthdayConfig {

    public static final BirthdayConfig INSTANCE = new BirthdayConfig();
    public  Path AutoHappyBirthdayConfigFile = FabricLoader.getInstance().getConfigDir().resolve("AutoHappyBirthday.json");
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public void save() {
        try {
            Files.deleteIfExists(AutoHappyBirthdayConfigFile);

            // Demo
            HashMap<UUID, String> Demonstrations = new HashMap<>();
            Demonstrations.put(UUID.fromString("acfcb375-e306-4acf-ae87-41bccb609557"), "21/11/2023");
            HashMap<String, Object> birthdaysFinal = new HashMap<>();
            birthdaysFinal.put("Birthdays", List.of(Demonstrations));
            Files.writeString(AutoHappyBirthdayConfigFile, gson.toJson(birthdaysFinal));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            if (Files.notExists(AutoHappyBirthdayConfigFile)) {
                save();
                return;
            }

            BirthdayList birthdayList = gson.fromJson(Files.readString(AutoHappyBirthdayConfigFile), BirthdayList.class);

            List<Map<String, String>> usersBirthdays = birthdayList.getBirthdays();
            for (Map<String, String> birthday : usersBirthdays) {
                for (Map.Entry<String, String> entry : birthday.entrySet()) {
                    UUID uuid = UUIDTypeAdapter.fromString(entry.getKey());
                    LocalDate date = LocalDate.parse(entry.getValue(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    birthdays.put(uuid, date);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
