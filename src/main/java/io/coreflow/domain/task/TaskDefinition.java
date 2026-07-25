package io.coreflow.domain.task;

import io.coreflow.domain.payloads.TaskPayload;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;
import java.util.Map;


@Getter
public final class TaskDefinition<T extends TaskPayload> {

    @NonNull private final TaskId taskId;
    @NonNull private final T taskPayload;
    @NonNull private final TaskPriority taskPriority;
    private final TaskStatus taskStatus;
    @NonNull private final WorkloadType workloadType;
    @NonNull private final Instant scheduledExecutionStartTime;
    private final Instant scheduledExecutionEndTime;
    private final long timeout;
    @NonNull private final RetryPolicy retryPolicy;

    private final String idempotencyKey;
    @NonNull private final Instant creationTimestamp;
    private final Map<String, String> metadata;

    @Builder
    private TaskDefinition(@NonNull TaskId taskId,
                          @NonNull T taskPayload,
                          @NonNull TaskPriority taskPriority,
                          TaskStatus taskStatus,
                          @NonNull WorkloadType workloadType,
                          @NonNull Instant scheduledExecutionStartTime,
                          Instant scheduledExecutionEndTime,
                          long timeout,
                          @NonNull RetryPolicy retryPolicy,
                          String idempotencyKey,
                          @NonNull Instant creationTimestamp,
                          Map<String, String> metadata) {
        this.taskId = taskId;
        this.taskPayload = taskPayload;
        this.taskPriority = taskPriority;
        this.taskStatus = taskStatus;
        this.workloadType = workloadType;
        this.scheduledExecutionStartTime = scheduledExecutionStartTime;
        this.scheduledExecutionEndTime = scheduledExecutionEndTime;
        this.timeout = timeout;
        this.retryPolicy = retryPolicy;
        this.idempotencyKey = idempotencyKey;
        this.creationTimestamp = creationTimestamp;
        this.metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}
