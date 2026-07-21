package io.coreflow.domain.hierarchy;

import io.coreflow.domain.task.TaskId;

import java.time.Instant;

public record CancellationResult(TaskId taskId, Instant cancellationTime, String reason) implements Hierarchy {

    public CancellationResult(TaskId taskId, Instant cancellationTime, String reason) {

        if(taskId == null){
            throw new IllegalArgumentException("taskId must not be null");
        }
        if(reason == null || reason.isBlank()){
            throw new IllegalArgumentException("reason must not be null or blank");
        }
        if(cancellationTime == null){
            throw new IllegalArgumentException("cancellationTime must not be null");
        }
        this.taskId = taskId;
        this.cancellationTime = cancellationTime;
        this.reason = reason;
    }

}
