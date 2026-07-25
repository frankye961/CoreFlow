package io.coreflow.domain.hierarchy;

import io.coreflow.domain.task.TaskId;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public final class FailureResult implements Hierarchy {

    private static final int MIN_ATTEMPTS = 1;

    private final TaskId taskId;
    private final ErrorSnapshot error;
    private final int attempt;
    private final boolean retryable;
    private final Instant failedAt;

    public FailureResult(TaskId taskId, Throwable error, int attempt, boolean retryable, Instant failedAt) {
        if (taskId == null) {
            throw new IllegalArgumentException("taskId must not be null");
        }
        if (error == null) {
            throw new IllegalArgumentException("error must not be null");
        }
        if (attempt < MIN_ATTEMPTS) {
            throw new IllegalArgumentException("Attempt must be at least " + MIN_ATTEMPTS);
        }
        if (failedAt == null) {
            throw new IllegalArgumentException("failedAt must not be null");
        }

        this.taskId = taskId;
        this.error = ErrorSnapshot.from(error);
        this.attempt = attempt;
        this.retryable = retryable;
        this.failedAt = failedAt;
    }

    public TaskId taskId() {
        return taskId;
    }

    public ErrorSnapshot error() {
        return error;
    }

    public int attempt() {
        return attempt;
    }

    public boolean retryable() {
        return retryable;
    }

    public Instant failedAt() {
        return failedAt;
    }

    public record ErrorSnapshot(String type, String message, List<StackTraceElement> stackTrace) {

        private static ErrorSnapshot from(Throwable error) {
            return new ErrorSnapshot(
                    error.getClass().getName(),
                    error.getMessage(),
                    Arrays.asList(error.getStackTrace()));
        }

        public ErrorSnapshot {
            stackTrace = List.copyOf(stackTrace);
        }
    }
}
