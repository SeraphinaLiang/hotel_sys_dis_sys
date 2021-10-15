package hotel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Room implements Serializable {

	private Integer roomNumber;
	private List<BookingDetail> bookings;
    // new attribute for concurrent write,whether the room is being writing
	private Semaphore writing = new Semaphore(1);

	public Room(Integer roomNumber) {
		this.roomNumber = roomNumber;
		bookings = new ArrayList<>();
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public List<BookingDetail> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingDetail> bookings) {
		this.bookings = bookings;
	}

    public void setWriting(boolean sign) throws InterruptedException {
        if (sign){
            writing.acquire();
        }else {
            writing.release();
        }
    }

}
