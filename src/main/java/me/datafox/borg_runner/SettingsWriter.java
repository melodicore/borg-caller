package me.datafox.borg_runner;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.OutputType;
import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;

/**
 * @author datafox
 */
public class SettingsWriter {
    public static void main(String[] args) {
        Json json = new Json(OutputType.json);
        json.setUsePrototypes(false);
        Settings settings = new Settings();
        settings.archives = new Archive[] { new Archive() };
        System.out.println(json.prettyPrint(settings));
    }
}
