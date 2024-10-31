package me.datafox.borg_runner;

import me.datafox.borg_runner.dto.Archive;
import me.datafox.borg_runner.dto.Settings;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

/**
 * @author datafox
 */
public class GraalReflectionFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        RuntimeReflection.register(Settings.class, Archive.class);
        RuntimeReflection.register(Settings.class.getDeclaredFields());
        RuntimeReflection.register(Archive.class.getDeclaredFields());
        RuntimeReflection.register(Settings.class.getDeclaredConstructors());
        RuntimeReflection.register(Archive.class.getDeclaredConstructors());
    }
}
