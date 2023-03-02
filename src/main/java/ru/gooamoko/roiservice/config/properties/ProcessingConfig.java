package ru.gooamoko.roiservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "processing")
public class ProcessingConfig {
    private final int threadsCount;
    private final int oldDaysCount;

    @ConstructorBinding
    public ProcessingConfig(Integer threadsCount, Integer oldDaysCount) {
        this.threadsCount = getOrDefault(threadsCount, 6);
        this.oldDaysCount = getOrDefault(oldDaysCount, 7);
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public int getOldDaysCount() {
        return oldDaysCount;
    }

    private int getOrDefault(Integer value, int defaultValue) {
        return (value == null || value <= 0) ? defaultValue : value;
    }
}
