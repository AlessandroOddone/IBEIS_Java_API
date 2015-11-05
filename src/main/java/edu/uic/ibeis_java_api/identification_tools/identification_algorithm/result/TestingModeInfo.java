package edu.uic.ibeis_java_api.identification_tools.identification_algorithm.result;

public class TestingModeInfo {

    private long executionTimeNanos;

    public TestingModeInfo(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
    }
}
