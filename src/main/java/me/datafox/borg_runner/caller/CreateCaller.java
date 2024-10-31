package me.datafox.borg_runner.caller;

import me.datafox.borg_runner.Utils;
import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;

import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public class CreateCaller extends Caller {
    public CreateCaller(Map<String,String> environment) {
        super("borg", "create", environment);
    }

    @Override
    protected void addArguments(List<String> args, Settings settings, Archive archive) {
        if(!Utils.checkArray(archive.directories)) {
            throw new IllegalArgumentException("Archive must have at least one directory");
        }
        if(!Utils.checkString(archive.name)) {
            throw new IllegalArgumentException("Archive must have a name");
        }
        if(settings.dryRun) {
            args.add("--dry-run");
        }
        if(archive.createStats) {
            args.add("--stats");
        }
        if(archive.createList) {
            args.add("--list");
        }
        if(Utils.checkString(archive.filter)) {
            args.add("--filter");
            args.add(archive.filter);
        }
        if(Utils.checkArray(archive.exclude)) {
            for(String exclude : archive.exclude) {
                args.add("--exclude");
                args.add(exclude);
            }
        }
        if(Utils.checkArray(archive.excludeFrom)) {
            for(String exclude : archive.excludeFrom) {
                args.add("--exclude-from");
                args.add(exclude);
            }
        }
        if(archive.excludeCaches) {
            args.add("--exclude-caches");
        }
        if(Utils.checkArray(archive.excludeIfPresent)) {
            for(String exclude : archive.excludeIfPresent) {
                args.add("--exclude-if-present");
                args.add(exclude);
            }
        }
        if(archive.keepExcludeTags) {
            args.add("--keep-exclude-tags");
        }
        if(Utils.checkArray(settings.extraFlags)) {
            args.addAll(List.of(settings.extraFlags));
        }
        if(Utils.checkArray(archive.extraCreateFlags)) {
            args.addAll(List.of(archive.extraCreateFlags));
        }
        args.add(Utils.archiveName(settings.prefix, archive.name, settings.suffix));
        args.addAll(List.of(archive.directories));
    }
}
