package dev.kiddo.autohappybirthday.birthday;

public class Birthday {
    private final String uuid;
    private final String date;

    public Birthday(String id, String date) {
        this.uuid = id;
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDate() {
        return date;
    }
}