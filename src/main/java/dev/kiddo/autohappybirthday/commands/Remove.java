package dev.kiddo.autohappybirthday.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.kiddo.autohappybirthday.client.utils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static dev.kiddo.autohappybirthday.client.AutoHappyBirthdayClient.getConfig;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Remove {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("ahb").then(literal("remove")
                        .then(argument("Player Name", string())
                                .executes(
                                        ctx -> {
                                            utils.removeUser(getString(ctx, "Player Name"));
                                            ctx.getSource().getPlayer().sendMessage(Text.of(Formatting.WHITE + "Removed " + Formatting.YELLOW + getString(ctx, "Player Name")));
                                            getConfig().load();
                                            return 1;
                                        }
                                )
                        )
                )
        );
    }
}