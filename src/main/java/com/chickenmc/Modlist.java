package com.chickenmc;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.Identifier;

import net.minecraft.server.permissions.PermissionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Modlist implements ModInitializer {
	public static final String MOD_ID = "modlist";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Map<String, ModMetadata> mods = new HashMap<>();
	public static Map<String, Short> masks = new HashMap<>();

	@Override
	public void onInitialize() {
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			mods.put(mod.getMetadata().getName(), mod.getMetadata());
			masks.put(mod.getMetadata().getName(), (short)0);
		}
		ArgumentTypeRegistry.registerArgumentType(
				id("field"),
				ModFieldArgumentType.class,
				SingletonArgumentInfo.contextFree(ModFieldArgumentType::new)
		);
		ArgumentTypeRegistry.registerArgumentType(
				id("mod_name"),
				ModNameArgumentType.class,
				SingletonArgumentInfo.contextFree(ModNameArgumentType::new)
		);
		CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, selection) -> {
			dispatcher.register(
					Commands.literal("mods")
							.then(Commands.literal("query")
									.then(Commands.argument("format", StringArgumentType.string())
											.then(Commands.argument("mod_name", new ModNameArgumentType()).suggests(new ModNameSuggestionProvider()).executes(CommandHandlers::modsQueryCommand))))
							.then(Commands.literal("mask").requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.mask"), PermissionLevel.ADMINS))
									.then(Commands.argument("field", new ModFieldArgumentType()).suggests(new ModFieldSuggestionProvider()).requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.mask"), PermissionLevel.ADMINS))
											.then(Commands.argument("mod_name", new ModNameArgumentType()).suggests(new ModNameSuggestionProvider()).requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.mask"), PermissionLevel.ADMINS)).executes(CommandHandlers::modsMaskCommand))))
							.then(Commands.literal("unmask").requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.unmask"), PermissionLevel.ADMINS))
									.then(Commands.argument("field", new ModFieldArgumentType()).suggests(new ModFieldSuggestionProvider()).requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.unmask"), PermissionLevel.ADMINS))
											.then(Commands.argument("mod_name", new ModNameArgumentType()).suggests(new ModNameSuggestionProvider()).requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.unmask"), PermissionLevel.ADMINS)).executes(CommandHandlers::modsUnmaskCommand))))
							.then(Commands.literal("mask_query").requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.mask_query"), PermissionLevel.ADMINS))
									.then(Commands.argument("field", new ModFieldArgumentType()).suggests(new ModFieldSuggestionProvider()).requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.mask_query"), PermissionLevel.ADMINS))
											.then(Commands.argument("mod_name", new ModNameArgumentType()).suggests(new ModNameSuggestionProvider()).requires(source -> source.checkPermission(Identifier.fromNamespaceAndPath(MOD_ID, "command.mods.mask_query"), PermissionLevel.ADMINS)).executes(CommandHandlers::modsMaskQueryCommand)))));
		}));
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
