package ru.gooamoko.roiservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "processing")
public class ProcessingConfig {
    private final Integer threadsCount;
    private final Integer oldDaysCount;

    @ConstructorBinding
    public ProcessingConfig(Integer threadsCount, Integer oldDaysCount) {
        this.threadsCount = threadsCount;
        this.oldDaysCount = oldDaysCount;
    }

    public Integer getThreadsCount() {
        return threadsCount;
    }

    public Integer getOldDaysCount() {
        return oldDaysCount;
    }
}
