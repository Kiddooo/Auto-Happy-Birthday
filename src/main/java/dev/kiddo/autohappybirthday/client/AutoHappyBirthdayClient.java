package dev.kiddo.autohappybirthday.client;

import dev.kiddo.autohappybirthday.commands.Add;
import dev.kiddo.autohappybirthday.commands.Query;
import dev.kiddo.autohappybirthday.commands.Remove;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class AutoHappyBirthdayClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Auto Happy Birthday");
    public static Map<UUID, LocalDate> birthdays = new HashMap<>();
    public static ArrayList<String> todaysBirthdays = new ArrayList<>();
    public static BirthdayConfig getConfig() {
        return BirthdayConfig.INSTANCE;
    }

    @Override
    public void onInitializeClient() {
        getConfig().load();
        LOGGER.info("Loaded Auto Happy Birthday");

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> Query.register(dispatcher)));
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> Add.register(dispatcher)));
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> Remove.register(dispatcher)));


    }
}