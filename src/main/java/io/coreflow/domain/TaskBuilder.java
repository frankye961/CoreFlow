package io.coreflow.domain;

import io.coreflow.domain.payloads.TaskPayload;
import io.coreflow.domain.task.RetryPolicy;
import io.coreflow.domain.task.TaskDefinition;
import io.coreflow.domain.task.TaskId;
import io.coreflow.domain.task.TaskPriority;
import io.coreflow.domain.task.TaskStatus;
import io.coreflow.domain.task.WorkloadType;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class TaskBuilder<T extends TaskPayload> {

    private TaskId taskId;
    private final T payload;
    private final TaskPriority priority;
    private TaskStatus taskStatus;
    private final WorkloadType workloadType;
    private Instant scheduledExecutionStartTime;
    private Instant scheduledExecutionEndTime;
    private final long timeout;
    private final RetryPolicy retryPolicy;
    private String idempotencyKey;
    private Instant creationTimestamp;
    private Map<String, String> metadata;
    private boolean built;

    TaskBuilder(T payload, TaskPriority priority, WorkloadType workloadType, long timeout, RetryPolicy retryPolicy) {
        this.payload = payload;
        this.priority = priority;
        this.workloadType = workloadType;
        this.timeout = timeout;
        this.retryPolicy = retryPolicy;
    }

    public TaskBuilder<T> withTaskId(TaskId taskId) {
        this.taskId = taskId;
        return this;
    }

    public TaskBuilder<T> withTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public TaskBuilder<T> withScheduledExecutionStartTime(Instant scheduledExecutionStartTime) {
        this.scheduledExecutionStartTime = scheduledExecutionStartTime;
        return this;
    }

    public TaskBuilder<T> withScheduledExecutionEndTime(Instant scheduledExecutionEndTime) {
        this.scheduledExecutionEndTime = scheduledExecutionEndTime;
        return this;
    }

    public TaskBuilder<T> withIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
        return this;
    }

    public TaskBuilder<T> withCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
        return this;
    }

    public TaskBuilder<T> withMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    public TaskDefinition<T> build() {
        if (built) {
            throw new IllegalStateException("TaskBuilder cannot be reused after build");
        }

        Instant creationTime = creationTimestamp == null ? Instant.now() : creationTimestamp;
        Instant scheduledTime = scheduledExecutionStartTime == null ? creationTime : scheduledExecutionStartTime;

        validate();
        TaskDefinition<T> taskDefinition = TaskDefinition.<T>builder()
                .taskId(taskId == null ? new TaskId(UUID.randomUUID()) : taskId)
                .taskPayload(payload)
                .taskPriority(priority)
                .taskStatus(taskStatus)
                .workloadType(workloadType)
                .scheduledExecutionStartTime(scheduledTime)
                .scheduledExecutionEndTime(scheduledExecutionEndTime)
                .timeout(timeout)
                .retryPolicy(retryPolicy)
                .idempotencyKey(idempotencyKey)
                .creationTimestamp(creationTime)
                .metadata(metadata == null ? Map.of() : metadata)
                .build();
        built = true;
        return taskDefinition;
    }

    private void validate() {
        Objects.requireNonNull(payload, "payload is null");
        Objects.requireNonNull(priority, "priority is null");
        Objects.requireNonNull(workloadType, "workloadType is null");
        Objects.requireNonNull(retryPolicy, "retryPolicy is null");
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout must be positive");
        }
        if (idempotencyKey != null && idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("idempotencyKey cannot be blank");
        }
        if (metadata != null) {
            metadata.forEach((key, value) -> {
                if (key == null || value == null) {
                    throw new IllegalArgumentException("metadata cannot contain null keys or values");
                }
            });
        }
    }
}
