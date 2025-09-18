package punto4_2;

//Estados por los que puede pasar el proceso
public enum State {
    CREATED,
    READY,
    EXECUTING,
    WAITING_FOR_RESOURCE,
    RECEIVING_IO,
    WAITING_IO,
    TERMINATED
}
