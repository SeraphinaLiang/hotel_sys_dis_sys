package hotel;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BookingManager implements Remote, Serializable {
// extends UnicastRemoteObject

    private Room[] rooms;

    // write and read
    private ReentrantReadWriteLock lockA = new ReentrantReadWriteLock();
    //  private ReentrantReadWriteLock lockB = new ReentrantReadWriteLock();

   // Semaphore readS = new Semaphore(3);

    public BookingManager() throws RemoteException {
        this.rooms = initializeRooms();
    }

    public Set<Integer> getAllRooms() throws RemoteException {
       lockA.readLock().lock();

        Set<Integer> allRooms = new HashSet<>();
        Iterable<Room> roomIterator = Arrays.asList(rooms);
        for (Room room : roomIterator) {
            allRooms.add(room.getRoomNumber());
        }

         lockA.readLock().unlock();
        return allRooms;
    }

    public boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException {
        //implement this method
         lockA.readLock().lock();

        Room currentRoom = null;
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                currentRoom = room;
                break;
            }
        }
        boolean isAvailable = true;
        List<BookingDetail> bookingDetails = currentRoom.getBookings();
        for (BookingDetail bd : bookingDetails) {
            if (bd.getDate().equals(date)) {
                isAvailable = false;
                break;
            }
        }
       lockA.readLock().unlock();

        return isAvailable;
    }

    public void addBooking(BookingDetail bookingDetail) throws Exception {
        //implement this method
        //if not booking the same room, can modify concurrently
        Room room = null;
        for (Room r : this.rooms) {
            if (r.getRoomNumber().equals(bookingDetail.getRoomNumber())) {
                room = r;
                break;
            }
        }
        List<BookingDetail> bookingList = room.getBookings();

        for (BookingDetail bd : bookingList) {
            try {
                if (bd.getDate().equals(bookingDetail.getDate())) {
                    throw new Exception("Required Room Not Available Exception"); // book fail
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**  block other read thread
         *   problem: only one thread can write
         *   can use synchronized(this){ ... }
         */
        lockA.writeLock().lock();

        room.setWriting(true);

        // room available
        bookingList.add(bookingDetail);
        room.setBookings(bookingList);

        room.setWriting(false);

        lockA.writeLock().unlock();
    }

    public Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
        //implement this method
        lockA.readLock().lock();

        List<Room> allRooms = Arrays.asList(rooms);
        Iterator<Room> iterator = allRooms.iterator();
        Set<Integer> availableRoomNumber = new HashSet<Integer>();

        while (iterator.hasNext()) {
            Room currentRoom = iterator.next();
            List<BookingDetail> bookingDetails = currentRoom.getBookings();
            int guests = 0;
            for (BookingDetail bd : bookingDetails) {
                if (bd.getDate().equals(date)) {
                    guests++;
                }
            }
            if (guests == 0) {
                availableRoomNumber.add(currentRoom.getRoomNumber());
            }
        }

        lockA.readLock().unlock();
        return availableRoomNumber;
    }

    private static Room[] initializeRooms() throws RemoteException {
        Room[] rooms = new Room[4];
        rooms[0] = new Room(101);
        rooms[1] = new Room(102);
        rooms[2] = new Room(201);
        rooms[3] = new Room(203);
        return rooms;
    }
}
