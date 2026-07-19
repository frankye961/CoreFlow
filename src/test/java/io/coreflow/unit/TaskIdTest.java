package io.coreflow.unit;

import io.coreflow.domain.TaskId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskIdTest {

    @Test
    public void testEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();

        TaskId first = new TaskId(uuid);
        TaskId second = new TaskId(uuid);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    public void testConstructorRejectsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new TaskId(null));

        assertThat(exception).hasMessage("taskId is null");
    }

    @Test
    public void testGenerateCreatesTaskIdWithValue() {
        TaskId seed = new TaskId(UUID.randomUUID());

        TaskId generated = seed.generate();

        assertThat(generated.value()).isNotNull();
    }

    @Test
    public void testGenerateCreatesDifferentTaskIds() {
        TaskId seed = new TaskId(UUID.randomUUID());

        TaskId first = seed.generate();
        TaskId second = seed.generate();

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void testGenerationFromString() {
        UUID uuidToTest = UUID.fromString("e326a5c7-79ae-42b4-93cc-124c1d4fb9c9");
        String uuidString = "e326a5c7-79ae-42b4-93cc-124c1d4fb9c9";
        TaskId taskFromString = new TaskId(uuidToTest);

        TaskId taskTransformedFromString = taskFromString.generateFromString(uuidString);

        assertThat(taskFromString).isEqualTo(taskTransformedFromString);
    }

    @Test
    public void testGenerateFromStringRejectsInvalidUUIDString() {
        UUID uuidToTest = UUID.fromString("e326a5c7-79ae-42b4-93cc-124c1d4fb9c9");
        String uuidString = "e326a5c7-79ae-42b4";
        TaskId taskFromString = new TaskId(uuidToTest);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskFromString.generateFromString(uuidString));

        assertThat(exception).hasMessage("Invalid TaskId: " + uuidString);
    }

    @Test
    public void testGenerateFromStringRejectsNull() {
        TaskId taskId = new TaskId(UUID.randomUUID());

        NullPointerException exception = assertThrows(NullPointerException.class, ()
                -> taskId.generateFromString(null));

        assertThat(exception).hasMessage("taskIdString is null");
    }
}
