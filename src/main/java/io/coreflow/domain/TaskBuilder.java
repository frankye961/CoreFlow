package io.coreflow.domain;


import io.coreflow.domain.payloads.TaskPayload;
import io.coreflow.domain.task.*;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class TaskBuilder<T extends TaskPayload> {

    private io.coreflow.domain.task.TaskId taskId;
    private T taskPayload;
    private TaskPriority taskPriority;
    private TaskStatus taskStatus;
    private WorkloadType workloadType;
    private Instant scheduledExecutionStartTime;
    private Instant scheduledExecutionEndTime;
    private long timeout;
    private RetryPolicy retryPolicy;

    private String idempotencyKey;
    private Instant creationTimestamp;
    private Map<String, String> metadata = new HashMap<>();



    private TaskId getTaskId() {
        return taskId == null ? new TaskId(UUID.randomUUID()) : taskId;
    }

    private TaskPriority getTaskPriority() {
        return switch (taskPriority) {
            case LOW -> TaskPriority.LOW;
            case HIGH -> TaskPriority.HIGH;
            case CRITICAL -> TaskPriority.CRITICAL;
            default -> TaskPriority.NORMAL;
        };
    }

    private Instant getCreationTimestamp() {
        if(creationTimestamp == null) {
            return Instant.now();
        }
        return creationTimestamp;
    }

    private long getTimeout() {
        if(timeout <= 0) {
            throw new IllegalArgumentException("Timeout must be greater than 0");
        }
        return timeout;
    }

    private Instant getScheduledExecutionStartTime(Instant creationTime) {
        return scheduledExecutionStartTime == null ? creationTime : scheduledExecutionStartTime;
    }

    private String getIdempotencyKey() {
        if (idempotencyKey != null && idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("idempotencyKey cannot be blank");
        }
        return idempotencyKey;
    }

    private Map<String, String> getMetadata() {
        if (metadata == null) {
            return Map.of();
        }
        metadata.forEach((k, v) -> {
            if (k == null || k.isBlank() || v == null || v.isBlank()) {
                throw new IllegalArgumentException("metadata cannot be null or blank");
            }
        });
        return metadata;
    }

    public TaskDefinition<T> build() {
        Instant creationTime = getCreationTimestamp();
        return TaskDefinition.<T>builder()
                .taskId(getTaskId())
                .taskPayload(taskPayload)
                .taskPriority(getTaskPriority())
                .taskStatus(taskStatus)
                .workloadType(workloadType)
                .scheduledExecutionStartTime(getScheduledExecutionStartTime(creationTime))
                .scheduledExecutionEndTime(scheduledExecutionEndTime)
                .timeout(getTimeout())
                .retryPolicy(retryPolicy)
                .idempotencyKey(getIdempotencyKey())
                .creationTimestamp(creationTime)
                .metadata(getMetadata())
                .build();
    }
}
