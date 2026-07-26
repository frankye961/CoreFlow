package io.coreflow.unit;

import io.coreflow.domain.hierarchy.FailureResult;
import io.coreflow.domain.hierarchy.Hierarchy;
import io.coreflow.domain.task.TaskId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FailureResultTest {

    @Test
    public void testFailureResultHappyPath() {
        TaskId taskId = new TaskId(UUID.randomUUID());
        Throwable error = new IllegalStateException("failure");
        Instant failedAt = Instant.now();

        FailureResult result = new FailureResult(taskId, error, 1, true, failedAt);

        assertThat(result.taskId()).isEqualTo(taskId);
        assertThat(result.error().type()).isEqualTo(IllegalStateException.class.getName());
        assertThat(result.error().message()).isEqualTo("failure");
        assertThat(result.attempt()).isEqualTo(1);
        assertThat(result.retryable()).isTrue();
        assertThat(result.failedAt()).isEqualTo(failedAt);
    }

    @Test
    public void testFailureResultCapturesAnImmutableErrorSnapshot() {
        Throwable error = new IllegalStateException("failure");
        List<StackTraceElement> originalStackTrace = List.of(error.getStackTrace());
        FailureResult result = new FailureResult(new TaskId(UUID.randomUUID()), error, 1, true, Instant.now());

        error.setStackTrace(new StackTraceElement[0]);

        assertThat(result.error().stackTrace()).isEqualTo(originalStackTrace);
        assertThrows(UnsupportedOperationException.class,
                () -> result.error().stackTrace().add(new StackTraceElement("Type", "method", "File", 1)));
    }

    @Test
    public void testFailureResultCanBeNonRetryable() {
        FailureResult result = new FailureResult(
                new TaskId(UUID.randomUUID()),
                new IllegalArgumentException("failure"),
                1,
                false,
                Instant.now());

        assertThat(result.retryable()).isFalse();
    }

    @Test
    public void testFailureResultIsHierarchy() {
        FailureResult result = new FailureResult(
                new TaskId(UUID.randomUUID()),
                new IllegalStateException("failure"),
                1,
                true,
                Instant.now());

        assertThat(result).isInstanceOf(Hierarchy.class);
    }

    @Test
    public void testRejectsNullTaskId() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FailureResult(null, new IllegalStateException("failure"), 1, true, Instant.now()));

        assertThat(exception).hasMessage("taskId must not be null");
    }

    @Test
    public void testRejectsNullError() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FailureResult(new TaskId(UUID.randomUUID()), null, 1, true, Instant.now()));

        assertThat(exception).hasMessage("error must not be null");
    }

    @Test
    public void testRejectsZeroAttempt() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FailureResult(
                        new TaskId(UUID.randomUUID()),
                        new IllegalStateException("failure"),
                        0,
                        true,
                        Instant.now()));

        assertThat(exception).hasMessage("Attempt must be at least 1");
    }

    @Test
    public void testRejectsNegativeAttempt() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FailureResult(
                        new TaskId(UUID.randomUUID()),
                        new IllegalStateException("failure"),
                        -1,
                        true,
                        Instant.now()));

        assertThat(exception).hasMessage("Attempt must be at least 1");
    }

    @Test
    public void testRejectsNullFailedAt() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FailureResult(
                        new TaskId(UUID.randomUUID()),
                        new IllegalStateException("failure"),
                        1,
                        true,
                        null));

        assertThat(exception).hasMessage("failedAt must not be null");
    }
}
