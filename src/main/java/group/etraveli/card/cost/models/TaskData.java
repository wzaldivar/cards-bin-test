package group.etraveli.card.cost.models;

import java.time.Instant;

public class TaskData {
    private String iin;
    private Instant timeStamp;

    public TaskData() {
    }

    public TaskData(String iin, Instant timeStamp) {
        this.iin = iin;
        this.timeStamp = timeStamp;
    }

    public String getIin() {
        return iin;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }
}
