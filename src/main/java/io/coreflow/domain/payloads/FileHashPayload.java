package io.coreflow.domain.payloads;

public record FileHashPayload(String filepath, String algorithm) implements TaskPayload {

    public FileHashPayload(String filepath,  String algorithm) {
        if (filepath == null || filepath.isBlank()) {
            throw new NullPointerException("filepath or is null or empty");
        }

        if (algorithm == null || algorithm.isBlank()) {
            throw new NullPointerException("algorithm or is null or empty");
        }

        this.filepath = filepath;
        this.algorithm = algorithm;
    }
}
