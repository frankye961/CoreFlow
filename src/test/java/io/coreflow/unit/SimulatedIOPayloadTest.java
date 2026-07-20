package io.coreflow.unit;

import io.coreflow.domain.payloads.SimulatedIOPayload;
import io.coreflow.domain.payloads.TaskPayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimulatedIOPayloadTest {

    @Test
    public void testZeroDelay() {
        SimulatedIOPayload payload = new SimulatedIOPayload(0, "response");

        assertThat(payload.delay()).isZero();
        assertThat(payload.response()).isEqualTo("response");
    }

    @Test
    public void testPositiveDelay() {
        SimulatedIOPayload payload = new SimulatedIOPayload(100, "response");

        assertThat(payload.delay()).isEqualTo(100);
        assertThat(payload.response()).isEqualTo("response");
    }

    @Test
    public void testSimulatedIOPayloadIsTaskPayload() {
        SimulatedIOPayload payload = new SimulatedIOPayload(0, "response");

        assertThat(payload).isInstanceOf(TaskPayload.class);
    }

    @Test
    public void testRejectsNegativeDelay() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SimulatedIOPayload(-1, "response"));

        assertThat(exception).hasMessage("delay is null or empty");
    }

    @Test
    public void testRejectsNullDelay() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SimulatedIOPayload(null, "response"));

        assertThat(exception).hasMessage("delay is null or empty");
    }

    @Test
    public void testRejectsNullResponse() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SimulatedIOPayload(0, null));

        assertThat(exception).hasMessage("response is null");
    }
}
