package com.bridgelabz.AddressBook;

public class IOTypeException extends Exception {
    enum ExceptionType {
        CSV_FILE_ISSUE, JSON_FILE_ISSUE
    }

    ExceptionType type;

    public IOTypeException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
