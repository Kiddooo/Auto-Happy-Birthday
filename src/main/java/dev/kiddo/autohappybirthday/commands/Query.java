package dev.kiddo.autohappybirthday.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.kiddo.autohappybirthday.client.utils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Query {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("ahb")
                        .then(literal("query")
                                .then(literal("today").executes(ctx -> {
                                    ArrayList<String> todaysBirthdays = utils.getTodaysBirthdays();
                                    if (todaysBirthdays.size() >= 1) {
                                        ctx.getSource().getPlayer().sendMessage(Text.of("Todays birthdays are: " + Formatting.YELLOW + String.join(", ", todaysBirthdays)));
                                    } else {
                                        ctx.getSource().getPlayer().sendMessage(Text.of("No birthdays today :("));
                                    }
                                    return 1;
                                }))
                                .then(literal("tomorrow").executes(ctx -> {
                                    ArrayList<String> tomorrowBirthdays = utils.getTomorrowBirthdays();
                                    if (tomorrowBirthdays.size() >= 1) {
                                        ctx.getSource().getPlayer().sendMessage(Text.of("Tomorrows birthdays are: " + Formatting.YELLOW + String.join(", ", tomorrowBirthdays)));
                                    } else {
                                        ctx.getSource().getPlayer().sendMessage(Text.of("No birthdays tomorrow :("));
                                    }
                                    return 1;
                                }))
                                .then(argument("Player Name", string()).executes(ctx -> {
                                    utils.getUserBirthday(getString(ctx, "Player Name")).thenAccept(birthday -> {
                                        if (birthday != null) {
                                            ctx.getSource().getPlayer().sendMessage(Text.of(Formatting.YELLOW + getString(ctx, "Player Name") + Formatting.WHITE + "'s birthday is on: " + birthday));
                                        } else {
                                            ctx.getSource().getPlayer().sendMessage(Text.of("Unable to find birthday information for: " + Formatting.YELLOW + getString(ctx, "Player Name")));
                                        }
                                    });
                                    return 1;
                                }))));
    }
}