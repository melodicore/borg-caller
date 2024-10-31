package me.datafox.borg_runner.caller;

import me.datafox.borg_runner.Utils;
import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;

import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public class CompactCaller extends Caller {
    public CompactCaller(Map<String,String> environment) {
        super("borg", "compact", environment);
    }

    @Override
    protected void addArguments(List<String> args, Settings settings, Archive archive) {
        if(Utils.checkArray(settings.extraFlags)) {
            args.addAll(List.of(settings.extraFlags));
        }
        if(Utils.checkArray(settings.extraCompactFlags)) {
            args.addAll(List.of(settings.extraCompactFlags));
        }
    }
}
