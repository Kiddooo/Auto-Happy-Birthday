package dev.kiddo.autohappybirthday.birthday;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class BirthdayList {
    @SerializedName("Birthdays")
    private List<Map<String, String>> birthdays;


    public List<Map<String, String>> getBirthdays() {
        return birthdays;
    }
}
