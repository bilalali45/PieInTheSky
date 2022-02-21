package pk.com.pieinthesky.app.beans;

import java.util.List;

public class ReservationTimings {

    private String TimeSlotId;
    private String TimeStart;
    private String TimeEnd;
    private int  SlotSpace;
    private int BookedSpace;
    private int AvailableSpace;

    public String getTimeSlotId() {
        return TimeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        TimeSlotId = timeSlotId;
    }

    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getTimeEnd() {
        return TimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        TimeEnd = timeEnd;
    }

    public int getSlotSpace() {
        return SlotSpace;
    }

    public void setSlotSpace(int slotSpace) {
        SlotSpace = slotSpace;
    }

    public int getBookedSpace() {
        return BookedSpace;
    }

    public void setBookedSpace(int bookedSpace) {
        BookedSpace = bookedSpace;
    }

    public int getAvailableSpace() {
        return AvailableSpace;
    }

    public void setAvailableSpace(int availableSpace) {
        AvailableSpace = availableSpace;
    }
}
