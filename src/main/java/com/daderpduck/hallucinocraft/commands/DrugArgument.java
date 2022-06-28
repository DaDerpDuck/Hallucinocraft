package com.daderpduck.hallucinocraft.commands;

import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.drugs.DrugRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class DrugArgument implements ArgumentType<Drug> {
    private static final Collection<String> EXAMPLES = Arrays.asList("hallucinocraft:brown_shrooms", "hallucinocraft:cannabis");
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_DRUG = new DynamicCommandExceptionType(function -> new TranslatableComponent("hallucinocraft.drug.drugNotFound", function));

    public static DrugArgument drug() {
        return new DrugArgument();
    }

    @Override
    public Drug parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation resourceLocation = ResourceLocation.read(reader);
        Drug drug = DrugRegistry.DRUGS.getValue(resourceLocation);
        if (drug != null) {
            return drug;
        } else {
            throw ERROR_UNKNOWN_DRUG.create(resourceLocation);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(DrugRegistry.DRUGS.getKeys(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static Drug getDrug(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, Drug.class);
    }
}
