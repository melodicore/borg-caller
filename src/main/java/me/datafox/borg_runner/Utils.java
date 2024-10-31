package me.datafox.borg_runner;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.OutputType;
import me.datafox.borg_runner.caller.Caller;
import me.datafox.borg_runner.caller.KeychainCaller;
import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;
import me.datafox.borg_runner.exception.NonZeroExitCodeException;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;

/**
 * @author datafox
 */
public class Utils {
    public static boolean checkString(String string) {
        return string != null && !string.isBlank();
    }

    public static <T> boolean checkArray(T[] array) {
        return array != null && array.length > 0;
    }

    public static boolean checkInt(int integer) {
        return integer > 0;
    }

    public static String archiveName(String prefix, String name, String suffix) {
        if(prefix == null) {
            prefix = "";
        }
        if(suffix == null) {
            suffix = "";
        }
        return "::" + prefix + name + suffix;
    }

    public static Settings loadSettings(String path) {
        System.out.println("Loading settings");
        try {
            return new Json(OutputType.json).fromJson(Settings.class, new File(path));
        } catch(Throwable e) {
            quit("Could not load or parse settings file", e.getMessage(), 2);
            return new Settings();
        }
    }

    public static void quit(String message, String cause, int status) {
        PrintStream out = status == 0 ? System.out : System.err;
        if(checkString(message)) {
            out.println(message);
        }
        if(checkString(message)) {
            out.println(cause);
        }
        System.exit(status);
    }

    public static void setSSHAgentEnvironment(Map<String,String> environment) {
        System.out.println("Setting SSH Agent environment variables");
        if(Utils.checkString(System.getenv("SSH_AUTH_SOCK")) &&
                Utils.checkString(System.getenv("SSH_AGENT_PID"))) {
            System.out.println("SSH Agent environment variables found");
            environment.put("SSH_AUTH_SOCK", System.getenv("SSH_AUTH_SOCK"));
            environment.put("SSH_AGENT_PID", System.getenv("SSH_AGENT_PID"));
            return;
        }
        System.out.println("Running keychain for SSH Agent environment variables");
        String output;
        try {
            Process p = new KeychainCaller(environment).call(null, null, false);
            int i = p.waitFor();
            if(i != 0) {
                throw new NonZeroExitCodeException("Keychain exited with code " + i);
            }
            output = new String(p.getInputStream().readAllBytes());
        } catch(Throwable e) {
            quit("Could not run keychain", e.getMessage(), 3);
            return;
        }
        String socket;
        String pid;
        try {
            socket = output.split("SSH_AUTH_SOCK=", 2)[1].split(";", 2)[0];
            pid = output.split("SSH_AGENT_PID=", 2)[1].split(";", 2)[0];
        } catch(Throwable e) {
            quit("Could not parse keychain output", e.getMessage(), 4);
            return;
        }
        System.out.println("SSH Agent environment variables retrieved from keychain");
        environment.put("SSH_AUTH_SOCK", socket);
        environment.put("SSH_AGENT_PID", pid);
    }

    public static void setBorgEnvironment(Settings settings, Map<String,String> environment) {
        System.out.println("Setting borg repository environment variable");
        if(Utils.checkString(settings.repo)) {
            System.out.println("Repository " + settings.repo + " found in settings");
            environment.put("BORG_REPO", settings.repo);
        } else if(Utils.checkString(System.getenv("BORG_REPO"))) {
            System.out.println("Repository " + System.getenv("BORG_REPO") + " found in environment variables");
            environment.put("BORG_REPO", System.getenv("BORG_REPO"));
        } else {
            Utils.quit("No repository in settings or environment variables", null, 5);
        }

        System.out.println("Setting borg passphrase environment variable");
        if(Utils.checkString(settings.passphrase)) {
            System.out.println("Passphrase found in settings");
            environment.put("BORG_PASSPHRASE", settings.passphrase);
        } else if(Utils.checkString(System.getenv("BORG_PASSPHRASE"))) {
            System.out.println("Passphrase found in environment variables");
            environment.put("BORG_PASSPHRASE", System.getenv("BORG_PASSPHRASE"));
        } else {
            Utils.quit("No passphrase in settings or environment variables", null, 6);
        }
    }

    public static void runCaller(Caller caller, Settings settings, Archive archive) {
        try {
            Process p = caller.call(settings, archive, true);
            int i = p.waitFor();
            if(i != 0) {
                throw new NonZeroExitCodeException("Borg exited with code " + i);
            }
        } catch(Throwable e) {
            quit("Could not run borg", e.getMessage(), 7);
        }
    }
}
