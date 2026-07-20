package io.coreflow.domain;

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
