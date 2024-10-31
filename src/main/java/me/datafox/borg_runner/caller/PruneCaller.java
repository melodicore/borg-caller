package me.datafox.borg_runner.caller;

import me.datafox.borg_runner.Utils;
import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;

import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public class PruneCaller extends Caller {
    public PruneCaller(Map<String,String> environment) {
        super("borg", "prune", environment);
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
        if(archive.pruneStats) {
            args.add("--stats");
        }
        if(archive.pruneList) {
            args.add("--list");
        }
        if(Utils.checkString(archive.keepWithin)) {
            args.add("--keep-within");
            args.add(archive.keepWithin);
        }
        if(Utils.checkInt(archive.keepSecondly)) {
            args.add("--keep-secondly");
            args.add(String.valueOf(archive.keepSecondly));
        }
        if(Utils.checkInt(archive.keepMinutely)) {
            args.add("--keep-minutely");
            args.add(String.valueOf(archive.keepMinutely));
        }
        if(Utils.checkInt(archive.keepHourly)) {
            args.add("--keep-hourly");
            args.add(String.valueOf(archive.keepHourly));
        }
        if(Utils.checkInt(archive.keepDaily)) {
            args.add("--keep-daily");
            args.add(String.valueOf(archive.keepDaily));
        }
        if(Utils.checkInt(archive.keepWeekly)) {
            args.add("--keep-weekly");
            args.add(String.valueOf(archive.keepWeekly));
        }
        if(Utils.checkInt(archive.keepMonthly)) {
            args.add("--keep-monthly");
            args.add(String.valueOf(archive.keepMonthly));
        }
        if(Utils.checkInt(archive.keepYearly)) {
            args.add("--keep-yearly");
            args.add(String.valueOf(archive.keepYearly));
        }
        if(Utils.checkArray(settings.extraFlags)) {
            args.addAll(List.of(settings.extraFlags));
        }
        if(Utils.checkArray(archive.extraPruneFlags)) {
            args.addAll(List.of(archive.extraPruneFlags));
        }
        args.add("--glob-archives");
        args.add(Utils.archiveName(settings.prefix, archive.name, settings.pruneSuffix));
    }
}
