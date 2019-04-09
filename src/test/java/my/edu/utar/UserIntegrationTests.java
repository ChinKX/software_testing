package my.edu.utar;

import java.util.ArrayList;
import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class UserIntegrationTests {
	private User user;
	
	Booking b1 = new Booking(user);
	Booking b2 = new Booking(user);
	Booking b3 = new Booking(user);
	
	@Before
	public void beforeTest() {
		// retrieve data and assign to variable
		user = new User("ABC", "vip", false);
	}
	
	@Test
	@Parameters(method = "paramsForBookRoomTest")
	public void bookRoomTest(int numOfRoomsBooked, Booking newBooking, Booking[] expectedResult) {
		Booking[] allBookings = new Booking[] { b1, b2 };
		user.setAllBooking(new ArrayList<Booking>(Arrays.asList(allBookings)));
		
		WaitingList waitingList = new WaitingList();
		Room allRooms = new Room(0, 0, 0);
		
		user.bookRoom(waitingList, allRooms, newBooking, numOfRoomsBooked);
		
		ArrayList<Booking> outputBookings = user.getAllBooking();
		Booking[] result = new Booking[outputBookings.size()];
		result = outputBookings.toArray(result);
		
		assertArrayEquals(expectedResult, result);
	}
	
	public Object[] paramsForBookRoomTest() {
		return new Object[] {
				new Object[] {
						1, b3,
						new Booking[] { b1, b2, b3 }
				}
		};
	}
	
	@Test
	@Parameters(method = "paramsForCancelRoomTest")
	public void cancelRoomTest(Booking targetBooking, Booking[] expectedResult) {
		Booking[] allBookings = new Booking[] { b1, b2, b3 };
		user.setAllBooking(new ArrayList<Booking>(Arrays.asList(allBookings)));
		
		WaitingList waitingList = new WaitingList();
		Room allRooms = new Room(0, 0, 0);
		
		user.cancelBooking(waitingList, allRooms, targetBooking);
		
		ArrayList<Booking> outputBookings = user.getAllBooking();
		Booking[] result = new Booking[outputBookings.size()];
		result = outputBookings.toArray(result);
		
		assertArrayEquals(expectedResult, result);
	}
	
	public Object[] paramsForCancelRoomTest() {
		return new Object[] {
				new Object[] {
						b2, new Booking[] { b1, b3 }
				}
		};
	}
}
