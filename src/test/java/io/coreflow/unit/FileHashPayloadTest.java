package io.coreflow.unit;

import io.coreflow.domain.payloads.FileHashPayload;
import io.coreflow.domain.payloads.TaskPayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileHashPayloadTest {

    @Test
    public void testFileHashPayloadHappyPath() {
        String path = "/test/test";
        String hash = "SHA-256";

        FileHashPayload fileHashPayload = new FileHashPayload(path, hash);

        assertThat(path).isEqualTo(fileHashPayload.filepath());
        assertThat(hash).isEqualTo(fileHashPayload.algorithm());
    }

    @Test
    public void testFileHashPayloadIsTaskPayload() {
        FileHashPayload fileHashPayload = new FileHashPayload("/test/test", "SHA-256");

        assertThat(fileHashPayload).isInstanceOf(TaskPayload.class);
    }

    @Test
    public void testRejectsNullFilepath() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new FileHashPayload(null, "SHA-256"));

        assertThat(exception).hasMessage("filepath or is null or empty");
    }

    @Test
    public void testRejectsBlankFilepath() {
        String path = "";
        String hash = "SHA-256";

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new FileHashPayload(path, hash));

        assertThat(exception).hasMessage("filepath or is null or empty");
    }

    @Test
    public void testRejectsWhitespaceOnlyFilepath() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new FileHashPayload(" ", "SHA-256"));

        assertThat(exception).hasMessage("filepath or is null or empty");
    }

    @Test
    public void testRejectsNullAlgorithm() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new FileHashPayload("/test/test", null));

        assertThat(exception).hasMessage("algorithm or is null or empty");
    }

    @Test
    public void testRejectsBlankAlgorithm() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new FileHashPayload("/test/test", " "));

        assertThat(exception).hasMessage("algorithm or is null or empty");
    }
}
