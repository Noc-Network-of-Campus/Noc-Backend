package com.mycompany.myapp.exception;

public class CustomExceptions {

    //테스트
    public static class testException extends RuntimeException{
        public testException(String message){
            super(message);
        }
    }

    public static class UnauthorizedAccessException extends RuntimeException {
        public UnauthorizedAccessException(String message) { super(message); }
    }
}
