package io.coreflow.domain.hierarchy;

import io.coreflow.domain.task.TaskId;

import java.time.OffsetDateTime;

public record SuccessResult(TaskId task,
                            String producedValue,
                            OffsetDateTime processedAt,
                            long executionTime) implements Hierarchy {

    public SuccessResult(TaskId task, String producedValue, OffsetDateTime processedAt, long executionTime) {

        if(task == null) throw new IllegalArgumentException("task must not be null");
        if(processedAt == null) throw new IllegalArgumentException("processedAt must not be null");

        if (producedValue == null) {
            throw new NullPointerException("producedValue is null");
        }
        if (executionTime < 0) {
            throw new IllegalArgumentException("executionTime must not be negative");
        }

        this.task = task;
        this.producedValue = producedValue;
        this.processedAt = processedAt;
        this.executionTime = executionTime;
    }
}
