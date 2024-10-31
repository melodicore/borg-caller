package me.datafox.borg_runner.dto;

/**
 * @author datafox
 */
public class Settings {
    public String repo = "";

    public String passphrase = "";

    public String prefix = "{hostname}-";

    public String suffix = "-{now}";

    public String pruneSuffix = "-*";

    public boolean dryRun = true;

    public String[] extraFlags = new String[0];

    public String[] extraCompactFlags = new String[0];

    public Archive[] archives = new Archive[0];
}
