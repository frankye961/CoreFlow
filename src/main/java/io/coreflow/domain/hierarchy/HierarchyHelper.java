package io.coreflow.domain.hierarchy;

public class HierarchyHelper {

    public Hierarchy processingHierarchy(Hierarchy hierarchy){
       return switch (hierarchy){
           case SuccessResult success -> success;
           case FailureResult failure -> failure;
           case CancellationResult cancellation -> cancellation;
           case TimeoutResult timeout -> timeout;
       };
    }
}
