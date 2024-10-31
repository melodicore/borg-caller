package me.datafox.borg_runner.caller;

import me.datafox.borg_runner.Utils;
import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public abstract class Caller {
    private final String command;

    private final String mode;

    private final Map<String, String> environment;

    protected Caller(String command, String mode, Map<String, String> environment) {
        this.command = command;
        this.mode = mode;
        this.environment = environment;
    }

    public Process call(Settings settings, Archive archive, boolean inheritIo) throws IOException {
        List<String> args = new ArrayList<>();
        if(Utils.checkString(command)) {
            args.add(command);
        }
        if(Utils.checkString(mode)) {
            args.add(mode);
        }
        addArguments(args, settings, archive);
        System.out.println("Running " + args);
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.environment().putAll(environment);
        if(inheritIo) {
            builder.inheritIO();
        }
        return builder.start();
    }

    protected abstract void addArguments(List<String> args, Settings settings, Archive archive);
}
