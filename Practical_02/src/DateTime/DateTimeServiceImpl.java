package DateTime;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeServiceImpl extends UnicastRemoteObject implements DateTimeService  {
	
	protected DateTimeServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String getDate() throws RemoteException {
        LocalDate date = LocalDate.now();
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public String getTime() throws RemoteException {
        LocalTime time = LocalTime.now();
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

	

}