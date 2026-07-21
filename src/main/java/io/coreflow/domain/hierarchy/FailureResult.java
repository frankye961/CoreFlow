package io.coreflow.domain.hierarchy;

import io.coreflow.domain.task.TaskId;

import java.time.Instant;

public record FailureResult(TaskId taskId, Throwable error, int attempt, boolean retryable, Instant failedAt)
        implements Hierarchy {

    private static final int MIN_ATTEMPTS = 1;

    public FailureResult(TaskId taskId, Throwable error, int attempt, boolean retryable, Instant failedAt) {

        if(taskId == null){
            throw new IllegalArgumentException("taskId must not be null");
        }
        if (attempt < MIN_ATTEMPTS) {
            throw new IllegalArgumentException("Attempt must be at least " + MIN_ATTEMPTS);
        }

        if(error == null) {
            throw new IllegalArgumentException("error must not be null");
        }

        if(failedAt == null) {
            throw new IllegalArgumentException("failedAt must not be null");
        }

        this.taskId = taskId;
        this.error = error;
        this.attempt = attempt;
        this.retryable = retryable;
        this.failedAt = failedAt;
    }
}
