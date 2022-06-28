package com.daderpduck.hallucinocraft.commands;

import com.daderpduck.hallucinocraft.capabilities.PlayerDrugs;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class SetDrugCommand {
    private SetDrugCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("drug").requires(source -> source.hasPermission(2))
                    .then(Commands.literal("give")
                        .then(Commands.argument("players", EntityArgument.players())
                            .then(Commands.argument("drug", DrugArgument.drug())
                                .executes(context -> setDrugValue(context.getSource(), EntityArgument.getPlayers(context, "players"), DrugArgument.getDrug(context, "drug"), 0.5F, 1200))
                                .then(Commands.argument("potency", FloatArgumentType.floatArg(0, 1))
                                    .executes(context -> setDrugValue(context.getSource(), EntityArgument.getPlayers(context, "players"), DrugArgument.getDrug(context, "drug"), FloatArgumentType.getFloat(context, "potency"), 1200))
                                    .then(Commands.argument("duration", IntegerArgumentType.integer(0))
                                        .executes(context -> setDrugValue(context.getSource(), EntityArgument.getPlayers(context, "players"), DrugArgument.getDrug(context, "drug"), FloatArgumentType.getFloat(context, "potency"), IntegerArgumentType.getInteger(context, "duration"))))))))
                    .then(Commands.literal("clear")
                        .then(Commands.argument("players", EntityArgument.players())
                            .executes(context -> clearDrug(context.getSource(), EntityArgument.getPlayers(context, "players")))))
        );
    }

    public static void registerSerializer() {
        ArgumentTypes.register("hallucinocraft:drug", DrugArgument.class, new EmptyArgumentSerializer<>(DrugArgument::drug));
    }

    private static int setDrugValue(CommandSourceStack source, Collection<ServerPlayer> players, Drug drug, float potency, int duration) {
        for (ServerPlayer player : players) {
            Drug.addDrug(player, new DrugInstance(drug, 0, potency, duration));
            PlayerDrugs.sync(player);
        }
        return 1;
    }

    private static int clearDrug(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            Drug.clearDrugs(player);
            PlayerDrugs.sync(player);
        }
        return 1;
    }
}
