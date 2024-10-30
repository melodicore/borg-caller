package me.datafox.borgcaller;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.JsonException;
import com.esotericsoftware.jsonbeans.OutputType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
public class BorgCaller {
    private static BorgSettings settings = null;
    private static String keychain_sock = null;
    private static String keychain_pid = null;

    public static void main(String[] args) {
        if(args.length == 0) {
            quit("No settings file given", null, 1);
        }
        System.out.println("Loading settings");
        String json = null;
        try {
            json = Files.readString(Path.of(args[0]));
        } catch(IOException e) {
            quit("Cannot read settings file " + args[0], e.getMessage(), 2);
        }
        try {
            settings = new Json(OutputType.json).fromJson(BorgSettings.class, json);
        } catch(JsonException e) {
            quit("Cannot deserialize settings file " + args[0], e.getMessage(), 3);
        }
        if(settings.dirs == null || settings.dirs.length == 0) {
            quit("No directories defined in settings file " + args[0], null, 0);
        }
        if(!System.getenv().containsKey("SSH_AUTH_SOCK") || !System.getenv().containsKey("SSH_AGENT_PID")) {
            keychain();
        }
        System.out.println("Running backup");
        for(BorgDirectory dir : settings.dirs) {
            create(dir);
        }
        for(BorgDirectory dir : settings.dirs) {
            prune(dir);
        }
        compact();
        System.out.println("Backup successful");
    }

    private static void keychain() {
        try {
            Process p = new ProcessBuilder("keychain", "--eval", "--quiet").start();
            p.waitFor();
            String str = new String(p.getInputStream().readAllBytes());
            keychain_sock = str.split("SSH_AUTH_SOCK=", 2)[1].split(";", 2)[0];
            keychain_pid = str.split("SSH_AGENT_PID=", 2)[1].split(";", 2)[0];
        } catch(InterruptedException | IOException e) {
            quit("Could not read keychain", e.getMessage(), 5);
        }
    }

    private static void create(BorgDirectory dir) {
        System.out.println("Creating archive " + dir.name);
        List<String> command = new ArrayList<>();
        command.add("borg");
        command.add("create");
        command.add("--verbose");
        command.add("--filter");
        command.add("AMCE");
        command.add("--list");
        command.add("--stats");
        command.add("--show-rc");
        command.add("--compression");
        command.add("lzma");
        command.add("--exclude-caches");
        for(String ex : dir.excludes) {
            command.add("--exclude");
            command.add(ex);
        }
        command.add("::{hostname}-" + dir.name + "-{now}");
        command.add(dir.dir);
        run(command);
    }

    private static void prune(BorgDirectory dir) {
        System.out.println("Pruning archive " + dir.name);
        List<String> command = new ArrayList<>();
        command.add("borg");
        command.add("prune");
        command.add("--list");
        command.add("--glob-archives");
        command.add("{hostname}-" + dir.name + "-*");
        command.add("--show-rc");
        if(dir.daily > 0) {
            command.add("--keep-daily");
            command.add(String.valueOf(dir.daily));
        }
        if(dir.weekly > 0) {
            command.add("--keep-weekly");
            command.add(String.valueOf(dir.weekly));
        }
        if(dir.monthly > 0) {
            command.add("--keep-monthly");
            command.add(String.valueOf(dir.monthly));
        }
        if(dir.yearly > 0) {
            command.add("--keep-yearly");
            command.add(String.valueOf(dir.yearly));
        }
        run(command);
    }

    private static void compact() {
        System.out.println("Compacting repository");
        List<String> command = new ArrayList<>();
        command.add("borg");
        command.add("compact");
        command.add("--verbose");
        run(command);
    }

    private static void run(List<String> command) {
        System.out.println("Running command " + String.join(" ", command));
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.environment().put("BORG_REPO", settings.repo);
        builder.environment().put("BORG_PASSPHRASE", settings.pass);
        if(keychain_sock == null) {
            builder.environment().put("SSH_AUTH_SOCK", keychain_sock);
            builder.environment().put("SSH_AGENT_PID", keychain_pid);
        }
        builder.inheritIO();
        try {
            int i = builder.start().waitFor();
            if(i != 0) {
                System.exit(i);
            }
        } catch(IOException | InterruptedException e) {
            quit("Could not run command " + String.join(" ", command), e.getMessage(), 4);
        }
    }

    private static void quit(String message, String cause, int status) {
        if(message != null && !message.isBlank()) {
            System.err.println(message);
        }
        if(cause != null && !cause.isBlank()) {
            System.err.println(cause);
        }
        System.exit(status);
    }
}
