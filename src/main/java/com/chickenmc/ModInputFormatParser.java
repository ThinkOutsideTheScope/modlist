package com.chickenmc;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ContactInformation;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ModInputFormatParser {
    private static List<Pair<String, Function<Pair<ModMetadata, Boolean>, String>>> patterns = new ArrayList<>();
    public static String modIdToModName(String modId) {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(modId);
        if (container.isPresent()) {
            return container.get().getMetadata().getName();
        }
        return modId;
    }
    private static List<String> personListToStringList(Collection<Person> people) {
        List<String> out = new ArrayList<>();
        for (Person p : people) {
            out.add(p.getName());
        }
        return out;
    }
    private static List<String> modDependencyListToStringList(Collection<ModDependency> deps) {
        List<String> out = new ArrayList<>();
        for (ModDependency dep : deps) {
            out.add(modIdToModName(dep.getModId()));
        }
        return out;
    }
    private static String bracketPattern(String input) {
        return "(?<!\\\\)\\{" + input + "\\}";
    }
    private static String getName(Pair<ModMetadata, Boolean> pair) {
        return pair.getA().getName();
    }
    private static String getId(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.ID) != 0) return "No ID available";
        return pair.getA().getId();
    }
    private static String getLicense(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.LICENSE) != 0) return "No license available";
        return String.join(",", pair.getA().getLicense());
    }
    private static String getHomepage(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.HOMEPAGE) != 0) return "No homepage available";
        return pair.getA().getContact().get("homepage").orElse("No homepage available");
    }
    private static String getAuthors(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.AUTHORS) != 0) return "No authors available";
        return String.join(",", personListToStringList(pair.getA().getAuthors()));
    }
    private static String getType(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.TYPE) != 0) return "No type available";
        return pair.getA().getType();
    }
    private static String getProvides(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.PROVIDES) != 0) return "No provides available";
        return String.join(",", pair.getA().getProvides());
    }
    private static String getVersion(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.VERSION) != 0) return "No version available";
        return pair.getA().getVersion().getFriendlyString();
    }
    private static String getEnvironment(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.ENVIRONMENT) != 0) return "No environment available";
        return pair.getA().getEnvironment().name();
    }
    private static String getDependencies(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.DEPENDENCIES) != 0) return "No dependencies available";
        return String.join(",", modDependencyListToStringList(pair.getA().getDependencies()));
    }
    private static String getDescription(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.DESCRIPTION) != 0) return "No description available";
        return pair.getA().getDescription();
    }
    private static String getContributors(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.CONTRIBUTORS) != 0) return "No contributors available";
        return String.join(",", personListToStringList(pair.getA().getContributors()));
    }
    private static String getSource(Pair<ModMetadata, Boolean> pair) {
        if (pair.getB() && (Modlist.masks.get(pair.getA().getName()) & ModMask.SOURCE) != 0) return "No source available";
        return pair.getA().getContact().get("source").orElse("No source available");
    }
    private static String getHomepageOrElseSource(Pair<ModMetadata, Boolean> pair) {
        short masks = (pair.getB() ? Modlist.masks.get(pair.getA().getName()) : 0);
        ContactInformation ci = pair.getA().getContact();
        Optional<String> homepage = (((masks & ModMask.HOMEPAGE) != 0) ? Optional.empty() : ci.get("homepage"));
        Optional<String> source   = (((masks & ModMask.SOURCE) != 0) ? Optional.empty() : ci.get("source"));
        return homepage.orElse(source.orElse("No homepage or source available"));
    }
    static {
        patterns.add(new Pair<>(bracketPattern("name"), ModInputFormatParser::getName));
        patterns.add(new Pair<>(bracketPattern("id"), ModInputFormatParser::getId));
        patterns.add(new Pair<>(bracketPattern("license"), ModInputFormatParser::getLicense));
        patterns.add(new Pair<>(bracketPattern("homepage"), ModInputFormatParser::getHomepage));
        patterns.add(new Pair<>(bracketPattern("authors"), ModInputFormatParser::getAuthors));
        patterns.add(new Pair<>(bracketPattern("type"), ModInputFormatParser::getType));
        patterns.add(new Pair<>(bracketPattern("provides"), ModInputFormatParser::getProvides));
        patterns.add(new Pair<>(bracketPattern("version"), ModInputFormatParser::getVersion));
        patterns.add(new Pair<>(bracketPattern("environment"), ModInputFormatParser::getEnvironment));
        patterns.add(new Pair<>(bracketPattern("dependencies"), ModInputFormatParser::getDependencies));
        patterns.add(new Pair<>(bracketPattern("description"), ModInputFormatParser::getDescription));
        patterns.add(new Pair<>(bracketPattern("contributors"), ModInputFormatParser::getContributors));
        patterns.add(new Pair<>(bracketPattern("source"), ModInputFormatParser::getSource));
        patterns.add(new Pair<>(bracketPattern("homepageOrElseSource"), ModInputFormatParser::getHomepageOrElseSource));
    }
    public static String format(String input, ModMetadata metadata, boolean hasAdmin) {
        String out = input;
        for (Pair<String, Function<Pair<ModMetadata, Boolean>, String>> pattern : patterns) {
            out = out.replaceAll(pattern.getA(), pattern.getB().apply(new Pair<>(metadata, !hasAdmin)));
        }
        return out;
    }
}
