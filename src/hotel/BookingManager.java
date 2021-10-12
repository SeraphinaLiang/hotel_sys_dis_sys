package hotel;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BookingManager {

    public static void main() throws RemoteException, NotBoundException {
        // do the rmi registry for the remote bm object, use by client

        // set security manager
        if (System.getSecurityManager() != null)
            System.setSecurityManager(null);

        // Lookup calculator from RMI registry
        Registry registry = LocateRegistry.getRegistry();
        BookingManager bookingManager = (BookingManager)registry.lookup("booking_manager");
        System.out.println("Booking Manager:" + bookingManager + " found from the RMI registry.");

    }

	private Room[] rooms;

	public BookingManager() {
		this.rooms = initializeRooms();
	}

	public Set<Integer> getAllRooms() {
		Set<Integer> allRooms = new HashSet<Integer>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);
		for (Room room : roomIterator) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

    // implement the actual method without using bm
    // 1. concurrency
    // 2. efficiency

	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) {
		//implement this method

		return false;
	}

    /**
     * thread safe
     * @param bookingDetail
     */
	public void addBooking(BookingDetail bookingDetail) {
		//implement this method
	}

	public Set<Integer> getAvailableRooms(LocalDate date) {
		//implement this method
		return null;
	}

	private static Room[] initializeRooms() {
		Room[] rooms = new Room[4];
		rooms[0] = new Room(101);
		rooms[1] = new Room(102);
		rooms[2] = new Room(201);
		rooms[3] = new Room(203);
		return rooms;
	}
}
