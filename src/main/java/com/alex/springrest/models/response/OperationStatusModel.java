package com.alex.springrest.models.response;

public class OperationStatusModel {
    private String operationName;
    private String operationResult;

    public OperationStatusModel(String operationName, String operationResult) {
        this.operationName = operationName;
        this.operationResult = operationResult;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }
}
