package io.coreflow.domain.hierarchy;

import io.coreflow.domain.task.TaskId;

import java.time.OffsetDateTime;

public record SuccessResult(TaskId task,
                            String producedValue,
                            OffsetDateTime processedAt,
                            long executionTime) implements Hierarchy {

    public SuccessResult(TaskId task, String producedValue, OffsetDateTime processedAt, long executionTime) {

        if (producedValue == null) {
            throw new NullPointerException("producedValue is null");
        }
        if (executionTime < 0) {
            throw new IllegalArgumentException("executionTime must be greater than 0");
        }

        this.task = task;
        this.producedValue = producedValue;
        this.processedAt = processedAt;
        this.executionTime = executionTime;
    }
}
