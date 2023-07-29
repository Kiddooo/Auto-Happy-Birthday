package dev.kiddo.autohappybirthday.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.kiddo.autohappybirthday.client.AutoHappyBirthdayClient.*;
import static dev.kiddo.autohappybirthday.client.BirthdayConfig.gson;

public class utils {


    public static ArrayList<String> getTodaysBirthdays() {
        ArrayList<String> todaysBirthdays = new ArrayList<>();
        for (Map.Entry<UUID, LocalDate> birthday : birthdays.entrySet()) {
            String todaysDate = formatTodaysDate(Calendar.getInstance(), "today");
            if (formatDate(LocalDate.parse(todaysDate)).equalsIgnoreCase(formatDate(birthday.getValue()))) {
                String username;
                try {
                    username = utils.convertUUIDToUsername(birthday.getKey().toString()).get();
                    todaysBirthdays.add(username);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return todaysBirthdays;
    }

    public static ArrayList<String> getTomorrowBirthdays() {
        ArrayList<String> tomorrowsBirthdays = new ArrayList<>();
        for (Map.Entry<UUID, LocalDate> birthday : birthdays.entrySet()) {
            String tomorrowsDate = formatTodaysDate(Calendar.getInstance(), "tomorrow");
            if (formatDate(LocalDate.parse(tomorrowsDate)).equalsIgnoreCase(formatDate(birthday.getValue()))) {
                String username;
                try {
                    username = utils.convertUUIDToUsername(birthday.getKey().toString()).get();
                    tomorrowsBirthdays.add(username);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return tomorrowsBirthdays;
    }

    public static CompletableFuture<String> getUserBirthday(String playerName) {
        return convertUsernameToUUID(playerName).thenCompose(uuid -> {
            CompletableFuture<String> future = new CompletableFuture<>();

            String filePath = Paths.get(getConfig().AutoHappyBirthdayConfigFile.toUri()).toString();
            try (FileReader fileReader = new FileReader(filePath);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray birthdaysArray = jsonObject.getAsJsonArray("Birthdays");

                for (JsonElement entry : birthdaysArray) {
                    JsonObject entryObject = entry.getAsJsonObject();
                    if (entryObject.has(uuid)) {
                        future.complete(entryObject.get(uuid).getAsString());
                        return future;
                    }
                }

            } catch (IOException e) {
                future.completeExceptionally(e);
            }

            future.complete(null);  // If the UUID is not found, complete the future with null
            return future;
        });
    }


    public static String formatTodaysDate(Calendar today, String date) {
        if (date.equals("today")) {
            return getYear(today) + "-" + getMonth(today) + "-" + getDay(today);
        } else if (date.equals("tomorrow")) {
            return getYear(today) + "-" + getMonth(today) + "-" + (Integer.parseInt(getDay(today)) + 1);
        }
        return null;
    }

    public static String getMonth(Calendar today) {
        if (today.get(Calendar.MONTH) <= 9) {
            return "0" + (today.get(Calendar.MONTH) + 1);
        } else {
            return String.valueOf(today.get(Calendar.MONTH) + 1);
        }
    }

    public static String getDay(Calendar today) {
        if (today.get(Calendar.DAY_OF_MONTH) <= 9) {
            return "0" + (today.get(Calendar.DAY_OF_MONTH));
        } else {
            return String.valueOf(today.get(Calendar.DAY_OF_MONTH));
        }
    }

    public static int getYear(Calendar today) {
        return today.get(Calendar.YEAR);
    }

    public static String formatDate(LocalDate date) {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date);
    }

    public static String formatUUID(Matcher matcher) {
        return matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3) + "-" + matcher.group(4) + "-" + matcher.group(5);
    }

    public static void removeUser(String playerName) {
        convertUsernameToUUID(playerName).thenAccept(result -> {
            String filePath = Paths.get(getConfig().AutoHappyBirthdayConfigFile.toUri()).toString();
            try (FileReader fileReader = new FileReader(filePath);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray birthdaysArray = jsonObject.getAsJsonArray("Birthdays");

                for (JsonElement entry : birthdaysArray) {
                    JsonObject entryObject = entry.getAsJsonObject();
                    if (entryObject.has(String.valueOf(result))) {
                        entryObject.remove(String.valueOf(result));
                        break;
                    }
                }

                HashMap<String, Object> birthdaysFinal = new HashMap<>();
                birthdaysFinal.put("Birthdays", birthdaysArray);

                try (FileWriter fileWriter = new FileWriter(filePath);
                     BufferedWriter writer = new BufferedWriter(fileWriter)) {
                    writer.write(gson.toJson(birthdaysFinal));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public static void addUser(String playerName, String date) {
        convertUsernameToUUID(playerName).thenAccept(result -> {
            String filePath = Paths.get(getConfig().AutoHappyBirthdayConfigFile.toUri()).toString();
            try (FileReader fileReader = new FileReader(filePath);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray birthdaysArray = jsonObject.getAsJsonArray("Birthdays");

                JsonObject firstEntry;
                if (birthdaysArray.size() > 0) {
                    firstEntry = birthdaysArray.get(0).getAsJsonObject();
                } else {
                    firstEntry = new JsonObject();
                    birthdaysArray.add(firstEntry);
                }
                LOGGER.info(String.valueOf(birthdaysArray.get(0).getAsJsonObject()));
                firstEntry.addProperty(String.valueOf(result), date);

                HashMap<String, Object> birthdaysFinal = new HashMap<>();
                birthdaysFinal.put("Birthdays", birthdaysArray);

                try (FileWriter fileWriter = new FileWriter(filePath);
                     BufferedWriter writer = new BufferedWriter(fileWriter)) {
                    writer.write(gson.toJson(birthdaysFinal));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<String> convertUsernameToUUID(String username) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
                InputStreamReader reader = new InputStreamReader(url.openStream());
                JsonElement element = JsonParser.parseReader(reader);
                if (element.isJsonObject()) {
                    String id = element.getAsJsonObject().get("id").getAsString();
                    final Pattern pattern = Pattern.compile("([a-f0-9]{8})([a-f0-9]{4})([0-5][0-9a-f]{3})([089ab][0-9a-f]{3})([0-9a-f]{12})");
                    Matcher matcher = pattern.matcher(id);
                    if (matcher.matches()) {
                        return formatUUID(matcher);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public static CompletableFuture<String> convertUUIDToUsername(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://api.mojang.com/user/profile/" + uuid);
                InputStreamReader reader = new InputStreamReader(url.openStream());
                JsonElement element = JsonParser.parseReader(reader);
                if (element.isJsonObject()) {
                    return element.getAsJsonObject().get("name").getAsString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

}
