package me.datafox.borg_runner.caller;

import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;

import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public class KeychainCaller extends Caller {
    public KeychainCaller(Map<String,String> environment) {
        super("keychain", null, environment);
    }

    @Override
    protected void addArguments(List<String> args, Settings settings, Archive archive) {
        args.add("--eval");
        args.add("--quiet");
    }
}
