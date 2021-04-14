package com.daderpduck.hallucinocraft.commands;

import com.daderpduck.hallucinocraft.capabilities.PlayerDrugs;
import com.daderpduck.hallucinocraft.client.rendering.shaders.ShaderRenderer;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugInstance;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

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
                    .then(Commands.literal("shaders")
                         .then(Commands.argument("enabled", BoolArgumentType.bool())
                            .executes(context -> toggleShaders(context.getSource(), BoolArgumentType.getBool(context, "enabled")))))
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

    private static int toggleShaders(CommandSource source, boolean enable) {
        if (FMLEnvironment.dist == Dist.CLIENT)
            if (ShaderRenderer.useShader != enable) {
                if (enable) {
                    ShaderRenderer.setup();
                } else {
                    ShaderRenderer.clear(false);
                }
            }
        return 1;
    }
}