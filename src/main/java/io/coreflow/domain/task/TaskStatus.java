package io.coreflow.domain.task;

public enum TaskStatus {
    CREATED,
    VALIDATING,
    QUEUED,
    SCHEDULED,
    DISPATCHED,
    RUNNING,
    RETRY_SCHEDULED,
    SUCCEEDED,
    FAILED,
    CANCELLED,
    TIMED_OUT,
    REJECTED
}
