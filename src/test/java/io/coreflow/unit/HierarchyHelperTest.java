package io.coreflow.unit;

import io.coreflow.domain.hierarchy.CancellationResult;
import io.coreflow.domain.hierarchy.FailureResult;
import io.coreflow.domain.hierarchy.HierarchyHelper;
import io.coreflow.domain.hierarchy.SuccessResult;
import io.coreflow.domain.hierarchy.TimeoutResult;
import io.coreflow.domain.task.TaskId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class HierarchyHelperTest {

    private final HierarchyHelper helper = new HierarchyHelper();

    @Test
    public void testProcessesSuccessResult() {
        SuccessResult result = new SuccessResult(new TaskId(UUID.randomUUID()), "result", OffsetDateTime.now(), 1);

        assertThat(helper.processingHierarchy(result)).isSameAs(result);
    }

    @Test
    public void testProcessesFailureResult() {
        FailureResult result = new FailureResult(
                new TaskId(UUID.randomUUID()),
                new IllegalStateException("failure"),
                1,
                true,
                Instant.now());

        assertThat(helper.processingHierarchy(result)).isSameAs(result);
    }

    @Test
    public void testProcessesCancellationResult() {
        CancellationResult result = new CancellationResult(new TaskId(UUID.randomUUID()), Instant.now(), "reason");

        assertThat(helper.processingHierarchy(result)).isSameAs(result);
    }

    @Test
    public void testProcessesTimeoutResult() {
        TimeoutResult result = new TimeoutResult(new TaskId(UUID.randomUUID()), Instant.now(), 1);

        assertThat(helper.processingHierarchy(result)).isSameAs(result);
    }
}
