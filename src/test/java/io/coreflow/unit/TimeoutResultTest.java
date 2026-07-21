package io.coreflow.unit;

import io.coreflow.domain.hierarchy.Hierarchy;
import io.coreflow.domain.hierarchy.TimeoutResult;
import io.coreflow.domain.task.TaskId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimeoutResultTest {

    @Test
    public void testTimeoutResultHappyPath() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        Instant timeout = Instant.now();
        long duration = 100;

        TimeoutResult result = new TimeoutResult(taskId, timeout, duration);

        assertThat(result.taskId()).isEqualTo(taskId);
        assertThat(result.timeout()).isEqualTo(timeout);
        assertThat(result.duration()).isEqualTo(duration);
    }

    @Test
    public void testTimeoutResultIsHierarchy() {
        TimeoutResult result = new TimeoutResult(new TaskId(UUID.randomUUID()), Instant.now(), 1);

        assertThat(result).isInstanceOf(Hierarchy.class);
    }

    @Test
    public void testRejectsNullTaskId() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimeoutResult(null, Instant.now(), 1));

        assertThat(exception).hasMessage("taskId must not be null");
    }

    @Test
    public void testRejectsNullTimeout() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimeoutResult(new TaskId(UUID.randomUUID()), null, 1));

        assertThat(exception).hasMessage("timeout must not be null");
    }

    @Test
    public void testRejectsZeroDuration() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimeoutResult(new TaskId(UUID.randomUUID()), Instant.now(), 0));

        assertThat(exception).hasMessage("duration must be greater than 0");
    }

    @Test
    public void testRejectsNegativeDuration() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimeoutResult(new TaskId(UUID.randomUUID()), Instant.now(), -1));

        assertThat(exception).hasMessage("duration must be greater than 0");
    }
}
