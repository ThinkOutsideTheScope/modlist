package com.chickenmc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ModFieldArgumentType {
    public static ModFieldType convert(String field) throws RuntimeException {
        String upper = field.toUpperCase();
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
    }
}
