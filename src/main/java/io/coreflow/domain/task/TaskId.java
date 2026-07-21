package io.coreflow.domain.task;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable task identifier wrapping a UUID.
 *
 * @param value unique task UUID
 */
public record TaskId(UUID value) {

    public TaskId(UUID value) {
        Objects.requireNonNull(value, "taskId is null");
        this.value = value;
    }

    public TaskId generate() {
        return new TaskId(UUID.randomUUID());
    }

    public TaskId generateFromString(String value) {
        try {
            Objects.requireNonNull(value, "taskIdString is null");
            return new TaskId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid TaskId: " + value);
        }
    }
}
