package dev.kiddo.autohappybirthday.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.kiddo.autohappybirthday.client.utils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static dev.kiddo.autohappybirthday.client.AutoHappyBirthdayClient.getConfig;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Add {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("ahb")
                        .then(literal("add")
                                .then(argument("Player Name", string())
                                        .then(argument("Birthdate [dd/mm/yyyy]", greedyString())
                                                .executes(
                                                        ctx -> {
                                                            utils.addUser(getString(ctx, "Player Name"), getString(ctx, "Birthdate [dd/mm/yyyy]"));
                                                            ctx.getSource().getPlayer().sendMessage(Text.of(Formatting.WHITE + "Added " + Formatting.YELLOW + getString(ctx, "Player Name") + Formatting.WHITE + " with birthdate " + Formatting.YELLOW + getString(ctx, "Birthdate [dd/mm/yyyy]")));
                                                            getConfig().load();
                                                            return 1;
                                                        }
                                                )
                                        )
                                )
                        )
        );
    }
}
