package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + "不存在，ID=" + id);
    }
}
