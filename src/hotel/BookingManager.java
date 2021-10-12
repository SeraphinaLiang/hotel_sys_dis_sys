package hotel;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.*;

public class BookingManager implements Remote {

    //--------------------RMI-binding---------------------------------------------
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        // set security manager if non existent
        if(System.getSecurityManager() != null)
            System.setSecurityManager(null);

        BookingManager bookingManager = new BookingManager();
        // do the rmi registry for the remote bm object, use by client
        Registry registry = LocateRegistry.getRegistry();
        BookingManager stub=(BookingManager) UnicastRemoteObject.exportObject(bookingManager,0);
        registry.bind("booking_manager", stub);
    }

    //----------------------------Server------------------------------------
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
        Room currentRoom = null;
        for (Room room : rooms){
            if (room.getRoomNumber().equals(roomNumber)){
                currentRoom = room;
                break;
            }
        }
        boolean isAvailable = true;
       List<BookingDetail> bookingDetails = currentRoom.getBookings();
        for (BookingDetail bd:bookingDetails){
            if (bd.getDate().equals(date)){
                isAvailable=false;
                break;
            }
        }

		return isAvailable;
	}


	public void addBooking(BookingDetail bookingDetail) throws Exception {
		//implement this method
        //if not booking the same room, can modify concurrently
        Room room=this.rooms[bookingDetail.getRoomNumber()];
        List<BookingDetail> bookingList = room.getBookings();

        for (BookingDetail bd:bookingList){
            if (bd.getDate().equals(bookingDetail.getDate())){
                // book fail
                throw new Exception("Required Room Not Available Exception");
            }
        }

        // room available
        bookingList.add(bookingDetail);
        room.setBookings(bookingList);

	}

	public Set<Integer> getAvailableRooms(LocalDate date) {
		//implement this method
        List<Room> allRooms = Arrays.asList(rooms);
        Iterator<Room> iterator = allRooms.iterator();
        Set<Integer> availableRoomNumber =new HashSet<Integer>();

        while (iterator.hasNext()){
            Room currentRoom = iterator.next();
            List<BookingDetail> bookingDetails = currentRoom.getBookings();
            int guests = 0;
            for (BookingDetail bd : bookingDetails){
                if (bd.getDate().equals(date)){
                    guests++;
                }
            }
            if (guests==0){
                availableRoomNumber.add(currentRoom.getRoomNumber());
            }
        }

		return availableRoomNumber;
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
