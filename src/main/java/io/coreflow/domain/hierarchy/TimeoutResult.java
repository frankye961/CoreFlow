package io.coreflow.domain.hierarchy;

import io.coreflow.domain.task.TaskId;

import java.time.Instant;

public record TimeoutResult (TaskId taskId, Instant timeout, long duration) implements Hierarchy{

    public TimeoutResult(TaskId taskId, Instant timeout, long duration){

        if(taskId == null){
            throw new IllegalArgumentException("taskId must not be null");
        }
        if(duration <= 0){
            throw new IllegalArgumentException("duration must be greater than 0");
        }

        if(timeout == null){
            throw new IllegalArgumentException("timeout must not be null");
        }

        this.taskId = taskId;
        this.timeout = timeout;
        this.duration = duration;
    }
}
