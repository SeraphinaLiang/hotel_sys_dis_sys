package staff;

import hotel.BookingDetail;
import hotel.BookingManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.Set;

public class BookingClient extends AbstractScriptedSimpleTest {

	private BookingManager bm = null;

	public static void main(String[] args) throws Exception {
		BookingClient client = new BookingClient();
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/
	public BookingClient() {
		try {
			//Look up the registered remote instance

            // set security manager
            if (System.getSecurityManager() != null)
                System.setSecurityManager(null);

            System.setProperty("java.rmi.server.hostname","localhost");
            // Lookup bookingManager object from RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost",8080);
            //Registry registry = LocateRegistry.getRegistry(1099);
            this.bm = (BookingManager)registry.lookup("booking");

            //this.bm = (BookingManager) Naming.lookup("rmi://127.0.0.1:1000/booking");

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	// use bm remote object to implement the following method
	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException {
		//Implement this method
        return bm.isRoomAvailable(roomNumber,date);
	}

	@Override
	public void addBooking(BookingDetail bookingDetail) throws Exception {
		//Implement this method
        bm.addBooking(bookingDetail);
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
		//Implement this method
		return bm.getAvailableRooms(date);
	}

	@Override
	public Set<Integer> getAllRooms() throws RemoteException {
		return bm.getAllRooms();
	}
}
