package com.chickenmc;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandHandlers {
    public static int modsQueryCommand(CommandContext<CommandSourceStack> context) {
        String fmt = context.getArgument("format", String.class);
        String mod = context.getArgument("mod_name", String.class);
        if (mod.equals("*")) {
            List<String> out = new ArrayList<>();
            for (Map.Entry<String, ModMetadata> entry : Modlist.mods.entrySet()) {
                out.add(ModInputFormatParser.format(fmt, entry.getValue(), context.getSource().permissions().hasPermission(Permissions.COMMANDS_ADMIN)));
            }
            context.getSource().sendSuccess(() -> Component.literal(String.join("\n", out)), false);
            return Command.SINGLE_SUCCESS;
        }
        ModMetadata metadata = Modlist.mods.get(mod);
        context.getSource().sendSuccess(() -> Component.literal(ModInputFormatParser.format(fmt, metadata, context.getSource().permissions().hasPermission(Permissions.COMMANDS_ADMIN))), false);
        return Command.SINGLE_SUCCESS;
    }
    private static boolean fieldIsMasked(ModFieldType field, String modName) {
        return switch (field) {
            case ID -> ((Modlist.masks.get(modName) & ModMask.ID) != 0);
            case LICENSE -> ((Modlist.masks.get(modName) & ModMask.LICENSE) != 0);
            case HOMEPAGE -> ((Modlist.masks.get(modName) & ModMask.HOMEPAGE) != 0);
            case AUTHORS -> ((Modlist.masks.get(modName) & ModMask.AUTHORS) != 0);
            case TYPE -> ((Modlist.masks.get(modName) & ModMask.TYPE) != 0);
            case PROVIDES -> ((Modlist.masks.get(modName) & ModMask.PROVIDES) != 0);
            case VERSION -> ((Modlist.masks.get(modName) & ModMask.VERSION) != 0);
            case ENVIRONMENT -> ((Modlist.masks.get(modName) & ModMask.ENVIRONMENT) != 0);
            case DEPENDENCIES -> ((Modlist.masks.get(modName) & ModMask.DEPENDENCIES) != 0);
            case DESCRIPTION -> ((Modlist.masks.get(modName) & ModMask.DESCRIPTION) != 0);
            case CONTRIBUTORS -> ((Modlist.masks.get(modName) & ModMask.CONTRIBUTORS) != 0);
            case SOURCE -> ((Modlist.masks.get(modName) & ModMask.SOURCE) != 0);
        };
    }
    private static void maskMod(ModFieldType field, String modName) {
        switch (field) {
            case ID -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.ID));
            case LICENSE -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.LICENSE));
            case HOMEPAGE -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.HOMEPAGE));
            case AUTHORS -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.AUTHORS));
            case TYPE -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.TYPE));
            case PROVIDES -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.PROVIDES));
            case VERSION -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.VERSION));
            case ENVIRONMENT ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.ENVIRONMENT));
            case DEPENDENCIES ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.DEPENDENCIES));
            case DESCRIPTION ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.DESCRIPTION));
            case CONTRIBUTORS ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.CONTRIBUTORS));
            case SOURCE ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) | ModMask.SOURCE));
        }
    }
    public static int modsMaskCommand(CommandContext<CommandSourceStack> context) {
        ModFieldType field = context.getArgument("field", ModFieldType.class);
        String modName = context.getArgument("mod_name", String.class);
        if (modName.equals("*")) {
            for (Map.Entry<String, ModMetadata> entry : Modlist.mods.entrySet()) {
                maskMod(field, entry.getKey());
            }
        } else {
            maskMod(field, modName);
        }
        return Command.SINGLE_SUCCESS;
    }
    private static void unmaskMod(ModFieldType field, String modName) {
        switch (field) {
            case ID -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.ID));
            case LICENSE -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.LICENSE));
            case HOMEPAGE -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.HOMEPAGE));
            case AUTHORS -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.AUTHORS));
            case TYPE -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.TYPE));
            case PROVIDES -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.PROVIDES));
            case VERSION -> Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.VERSION));
            case ENVIRONMENT ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.ENVIRONMENT));
            case DEPENDENCIES ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.DEPENDENCIES));
            case DESCRIPTION ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.DESCRIPTION));
            case CONTRIBUTORS ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.CONTRIBUTORS));
            case SOURCE ->
                    Modlist.masks.put(modName, (short) (Modlist.masks.get(modName) & ~ModMask.SOURCE));
        }
    }
    public static int modsUnmaskCommand(CommandContext<CommandSourceStack> context) {
        ModFieldType field = context.getArgument("field", ModFieldType.class);
        String modName = context.getArgument("mod_name", String.class);
        if (modName.equals("*")) {
            for (Map.Entry<String, ModMetadata> entry : Modlist.mods.entrySet()) {
                unmaskMod(field, entry.getKey());
            }
        } else {
            unmaskMod(field, modName);
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int modsMaskQueryCommand(CommandContext<CommandSourceStack> context) {
        ModFieldType field = context.getArgument("field", ModFieldType.class);
        String modName = context.getArgument("mod_name", String.class);
        if (modName.equals("*")) {
            for (Map.Entry<String, ModMetadata> entry : Modlist.mods.entrySet()) {
                if (fieldIsMasked(field, entry.getKey())) {
                    context.getSource().sendSuccess(() -> Component.literal(entry.getKey() + ": Field is masked"), false);
                } else {
                    context.getSource().sendSuccess(() -> Component.literal(entry.getKey() + ": Field is not masked"), false);
                }
            }
        } else {
            if (fieldIsMasked(field, modName)) {
                context.getSource().sendSuccess(() -> Component.literal("Field is masked"), false);
            } else {
                context.getSource().sendSuccess(() -> Component.literal("Field is not masked"), false);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
