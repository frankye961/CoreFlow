package io.coreflow.domain.payloads;

public record SimulatedIOPayload(Integer delay, String response) implements TaskPayload {

    public SimulatedIOPayload(Integer delay, String response) {
        if (delay == null ||  delay < 0) {
            throw new IllegalArgumentException("delay is null or empty");
        }
        if (response == null) throw new IllegalArgumentException("response is null");
        this.delay = delay;
        this.response = response;
    }
}
