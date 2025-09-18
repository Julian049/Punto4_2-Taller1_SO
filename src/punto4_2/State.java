package punto4_2;

public enum State {
    CREATED,
    READY,
    EXECUTING,
    WAITING_FOR_RESOURCE,
    RECEIVING_IO,
    WAITING_IO,
    TERMINATED
}
