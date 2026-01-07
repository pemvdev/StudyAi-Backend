package com.example.study_ai.exceptions;

public class ClassroomNotFoundException extends RuntimeException {
    public ClassroomNotFoundException() {
        super("Classroom not found");
    }

    public ClassroomNotFoundException(Long id) {
        super("Classroom with id " + id + " not found");
    }

}
