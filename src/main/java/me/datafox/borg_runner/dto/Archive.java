package me.datafox.borg_runner.dto;

/**
 * @author datafox
 */
public class Archive {
    public String[] directories = new String[0];

    public String name = "";

    public boolean createStats = true;

    public boolean createList = true;

    public String filter = "";

    public String[] exclude = new String[0];

    public String[] excludeFrom = new String[0];

    public boolean excludeCaches = true;

    public String[] excludeIfPresent = new String[0];

    public boolean keepExcludeTags = false;

    public String[] extraCreateFlags = new String[0];

    public boolean pruneStats = true;

    public boolean pruneList = true;

    public String keepWithin = "";

    public int keepSecondly = 0;

    public int keepMinutely = 0;

    public int keepHourly = 0;

    public int keepDaily = 7;

    public int keepWeekly = 0;

    public int keepMonthly = 0;

    public int keepYearly = 0;

    public String[] extraPruneFlags = new String[0];
}
