package com.chickenmc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ModFieldArgumentType implements ArgumentType<ModFieldType> {
    @Override
    public ModFieldType parse(StringReader reader) throws CommandSyntaxException {
        try {
            String string = reader.readString();
            String upper = string.toUpperCase();
            return switch (upper) {
                case "ID" -> ModFieldType.ID;
                case "LICENSE" -> ModFieldType.LICENSE;
                case "HOMEPAGE" -> ModFieldType.HOMEPAGE;
                case "AUTHORS" -> ModFieldType.AUTHORS;
                case "TYPE" -> ModFieldType.TYPE;
                case "PROVIDES" -> ModFieldType.PROVIDES;
                case "VERSION" -> ModFieldType.VERSION;
                case "ENVIRONMENT" -> ModFieldType.ENVIRONMENT;
                case "DEPENDENCIES" -> ModFieldType.DEPENDENCIES;
                case "DESCRIPTION" -> ModFieldType.DESCRIPTION;
                case "CONTRIBUTORS" -> ModFieldType.CONTRIBUTORS;
                case "SOURCE" -> ModFieldType.SOURCE;
                default -> throw new RuntimeException();
            };
        } catch (Exception e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("Invalid mod field type");
        }
    }
}
