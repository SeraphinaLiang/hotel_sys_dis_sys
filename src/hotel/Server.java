package hotel;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    //--------------------RMI-binding---------------------------------------------
    public static void main(String[] args) throws RemoteException {

        BookingManager bookingManager = new BookingManager();
        // set security manager if non existent
        if (System.getSecurityManager() != null)
            System.setSecurityManager(null);

        System.setProperty("java.rmi.server.hostname","localhost");
        try {
            // do the rmi registry for the remote bm object, use by client
            //Registry registry = LocateRegistry.getRegistry();

            Registry registry = LocateRegistry.createRegistry(8080);

            /**-----problem--------
             * BookingManager need to extends UnicastRemoteObject, change bookingManager to stub.
             * but--- throw object already exported Exception
             * add a BookingManager Interface implements Remote
             */
            //BookingManagerI stub = (BookingManagerI) UnicastRemoteObject.exportObject(bookingManager, 0);

            //java.rmi.Naming.rebind("rmi://127.0.0.1:1000/booking", stub);

            registry.rebind("booking", bookingManager);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
