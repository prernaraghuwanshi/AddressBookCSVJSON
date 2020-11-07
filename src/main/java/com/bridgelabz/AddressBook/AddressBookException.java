package com.bridgelabz.AddressBook;

public class AddressBookException extends Exception{
    enum ExceptionType {
        QUERY_ISSUE, CONNECTION_ISSUE, THREAD_ISSUE, COMMIT_ISSUE, ROLLBACK_ISSUE
    }

    ExceptionType type;

    public AddressBookException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
