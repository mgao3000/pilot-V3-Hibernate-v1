package com.devmountain.training.exception;

public class TrainingEntitySaveOrUpdateFailedException extends RuntimeException {

    private static final long serialVersionUID = -156211082149877115L;

    public TrainingEntitySaveOrUpdateFailedException() {
        super();
    }

    public TrainingEntitySaveOrUpdateFailedException(String arg0) {
        super(arg0);
    }

    public TrainingEntitySaveOrUpdateFailedException(Throwable cause) {
        super(cause);
    }

    public TrainingEntitySaveOrUpdateFailedException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

}
