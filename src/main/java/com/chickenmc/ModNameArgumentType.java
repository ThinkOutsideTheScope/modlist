package com.chickenmc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ModNameArgumentType implements ArgumentType<String> {
    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        try {
            String modName = reader.readString();
            if (!modName.equals("*") && !Modlist.mods.containsKey(modName)) throw new RuntimeException();
            return modName;
        } catch (Exception e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("Invalid mod name");
        }
    }
}
