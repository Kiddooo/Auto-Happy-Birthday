package dev.kiddo.autohappybirthday.client;

import dev.kiddo.autohappybirthday.commands.Add;
import dev.kiddo.autohappybirthday.commands.Query;
import dev.kiddo.autohappybirthday.commands.Remove;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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

    public static BirthdayConfig getConfig() {
        return BirthdayConfig.INSTANCE;
    }

    @Override
    public void onInitializeClient() {
        ArrayList<String> todaysBirthdays = utils.getTodaysBirthdays();
        ArrayList<String> tomorrowBirthdays = utils.getTomorrowBirthdays();
        getConfig().load();
        LOGGER.info("Loaded Auto Happy Birthday");

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> Query.register(dispatcher)));
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> Add.register(dispatcher)));
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> Remove.register(dispatcher)));

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {

            if (MinecraftClient.getInstance().player == null) {
                LOGGER.error("Player is null si");
                return;
            }

            if (todaysBirthdays.size() >= 1) {
                client.player.sendMessage(Text.of("Todays birthdays are: " + Formatting.YELLOW + String.join(", ", todaysBirthdays)));
            }

            if (tomorrowBirthdays.size() >= 1) {
                client.player.sendMessage(Text.of("Tomorrows birthdays are: " + Formatting.YELLOW + String.join(", ", tomorrowBirthdays)));

            }
        }));

    }
}