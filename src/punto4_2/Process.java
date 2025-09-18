package punto4_2;

import java.util.ArrayList;

public class Process {
    private String name;
    private int size;
    private State state;

    public Process(String name, int size) {
        this.name = name;
        this.size = size;
        this.state = State.CREATED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
