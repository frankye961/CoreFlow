package io.coreflow.unit;

import io.coreflow.domain.hierarchy.CancellationResult;
import io.coreflow.domain.hierarchy.Hierarchy;
import io.coreflow.domain.task.TaskId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CancellationResultTest {

    @Test
    public void testCancellationResultHappyPath() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        Instant cancellationTime = Instant.now();
        String reason = "user requested cancellation";

        CancellationResult result = new CancellationResult(taskId, cancellationTime, reason);

        assertThat(result.taskId()).isEqualTo(taskId);
        assertThat(result.cancellationTime()).isEqualTo(cancellationTime);
        assertThat(result.reason()).isEqualTo(reason);
    }

    @Test
    public void testCancellationResultIsHierarchy() {
        CancellationResult result = new CancellationResult(
                new TaskId(UUID.randomUUID()),
                Instant.now(),
                "shutdown");

        assertThat(result).isInstanceOf(Hierarchy.class);
    }

    @Test
    public void testRejectsNullTaskId() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CancellationResult(null, Instant.now(), "reason"));

        assertThat(exception).hasMessage("taskId must not be null");
    }

    @Test
    public void testRejectsNullCancellationTime() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CancellationResult(new TaskId(UUID.randomUUID()), null, "reason"));

        assertThat(exception).hasMessage("cancellationTime must not be null");
    }

    @Test
    public void testRejectsNullReason() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CancellationResult(new TaskId(UUID.randomUUID()), Instant.now(), null));

        assertThat(exception).hasMessage("reason must not be null or blank");
    }

    @Test
    public void testRejectsBlankReason() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CancellationResult(new TaskId(UUID.randomUUID()), Instant.now(), ""));

        assertThat(exception).hasMessage("reason must not be null or blank");
    }

    @Test
    public void testRejectsWhitespaceOnlyReason() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CancellationResult(new TaskId(UUID.randomUUID()), Instant.now(), " "));

        assertThat(exception).hasMessage("reason must not be null or blank");
    }
}
