package hotel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {

	private Integer roomNumber;
	private List<BookingDetail> bookings;

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

    /**
     * when write, all read block.
     * allow concurrent write for different room
     * @param bookingDetail
     */
	public synchronized void setBookings(BookingDetail bookingDetail) {
	    BookingManager.lockA.writeLock().lock();
		this.bookings.add(bookingDetail);
		BookingManager.lockA.writeLock().unlock();
	}


}
