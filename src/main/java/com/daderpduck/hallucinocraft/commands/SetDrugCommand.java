package com.daderpduck.hallucinocraft.commands;

import com.daderpduck.hallucinocraft.capabilities.PlayerDrugs;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Collection;

public class SetDrugCommand {
    private SetDrugCommand() {}

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("drug").requires(source -> source.hasPermission(2))
                    .then(Commands.literal("give")
                        .then(Commands.argument("players", EntityArgument.players())
                            .then(Commands.argument("drug", new DrugArgument())
                                .executes(context -> setDrugValue(context.getSource(), EntityArgument.getPlayers(context, "players"), DrugArgument.getDrug(context, "drug"))))))
                    .then(Commands.literal("clear")
                        .then(Commands.argument("players", EntityArgument.players())
                            .executes(context -> clearDrug(context.getSource(), EntityArgument.getPlayers(context, "players")))))
        );
    }

    private static int setDrugValue(CommandSource source, Collection<ServerPlayerEntity> players, Drug drug) {
        for (ServerPlayerEntity player : players) {
            Drug.addDrug(player, new DrugInstance(drug, 0, 10F, 1200));
            PlayerDrugs.sync(player);
        }
        return 1;
    }

    private static int clearDrug(CommandSource source, Collection<ServerPlayerEntity> players) {
        for (ServerPlayerEntity player : players) {
            Drug.clearDrugs(player);
            PlayerDrugs.sync(player);
        }
        return 1;
    }
}
