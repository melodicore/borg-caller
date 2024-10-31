package me.datafox.borg_runner;

import me.datafox.borg_runner.caller.CompactCaller;
import me.datafox.borg_runner.caller.CreateCaller;
import me.datafox.borg_runner.caller.PruneCaller;
import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * @author datafox
 */
public class Runner {
    public static void main(String[] args) {
        if(!Utils.checkArray(args)) {
            Utils.quit("No settings file argument given", null, 1);
        }

        Settings settings = Utils.loadSettings(args[0]);

        if(!Utils.checkArray(settings.archives)) {
            Utils.quit("No archives in settings", null, 0);
        }

        Map<String, String> environment = new HashMap<>();
        Utils.setSSHAgentEnvironment(environment);
        Utils.setBorgEnvironment(settings, environment);

        for(Archive archive : settings.archives) {
            Utils.runCaller(new CreateCaller(environment), settings, archive);
        }

        for(Archive archive : settings.archives) {
            Utils.runCaller(new PruneCaller(environment), settings, archive);
        }

        if(!settings.dryRun) {
            Utils.runCaller(new CompactCaller(environment), settings, null);
        }
    }

}
