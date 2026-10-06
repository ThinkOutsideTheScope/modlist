package com.chickenmc;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;

import java.util.concurrent.CompletableFuture;

public class ModFieldSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        builder.suggest("id");
        builder.suggest("license");
        builder.suggest("homepage");
        builder.suggest("authors");
        builder.suggest("type");
        builder.suggest("provides");
        builder.suggest("version");
        builder.suggest("environment");
        builder.suggest("dependencies");
        builder.suggest("description");
        builder.suggest("contributors");
        builder.suggest("source");
        return builder.buildFuture();
    }
}
