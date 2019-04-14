package Booking.test;

import Booking.system.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class UserUnitTests {
	User ur = new User("Apple", "vip", true);;
	
	Booking b1 = new Booking(ur);;
	Booking b2 = new Booking(ur);;
	Booking b3 = new Booking(ur);;
	
	// set User's booking records
	//u1
	@Test
	@Parameters(method = "paramsForGetSetAllBookingTest")
	public void getSetAllBookingTest(Booking[] allBooking, Booking[] expectedResult) {
		ur.setAllBooking(allBooking);
		
		Booking[] result = new Booking[ur.getAllBooking().size()];
		result = ur.getAllBooking().toArray(result);
		
		assertArrayEquals(expectedResult, result);
	}
	
	private Object[] paramsForGetSetAllBookingTest() {
		return new Object[] {
			new Object[] {
				new Booking[] { b1, b2, b3 },
				new Booking[] { b1, b2, b3 }
			}
		};
	}
	
	// set user's exclusive reward
	//u2
	@Test
	@Parameters({
		"false, false",
		"true, true"
	})
	public void getSetExclReward(boolean input, boolean expectedResult) {
		ur.set_excl_reward(input);
		assertEquals(expectedResult, ur.get_excl_reward());
	}
	
	//test if user book room successfully or fail 
	//u3
	@Test
	@Parameters(method = "paramsForBookRoomTest")
	public void bookRoomTest(int numOfRoomsBooked, boolean[][] IsRoomAvailable, 
			Booking[] initialList, Booking[] expectedList) {		
		Booking newBooking = mock(Booking.class);	
		
		Printer displayBooking = mock(Printer.class);
		WaitingList wl = mock(WaitingList.class);
		Room allRooms = mock(Room.class);

		when(allRooms.checkRoom(eq("vip"), anyInt())).thenAnswer(new Answer() {
			int count = 0;
			
			public Object answer(InvocationOnMock invocation) {
				if(count < IsRoomAvailable.length)
					return IsRoomAvailable[count++][0];
				
				return IsRoomAvailable[count - 1][0];
			}
		});
		when(allRooms.checkRoom(eq("member"), anyInt())).thenAnswer(new Answer() {
			int count = 0;
			
			public Object answer(InvocationOnMock invocation) {
				if(count < IsRoomAvailable.length)
					return IsRoomAvailable[count++][1];
				
				return IsRoomAvailable[count - 1][1];
			}
		});
		when(allRooms.checkRoom(eq("nonMember"), anyInt())).thenAnswer(new Answer() {
			int count = 0;
			
			public Object answer(InvocationOnMock invocation) {
				if(count < IsRoomAvailable.length)
					return IsRoomAvailable[count++][2];
				
				return IsRoomAvailable[count - 1][2];
			}
		});
		
		ur.setAllBooking(initialList);
		
		ur.bookRoom(displayBooking, wl, allRooms, newBooking, numOfRoomsBooked);
		
		Booking[] result = new Booking[ur.getAllBooking().size()];
		result = ur.getAllBooking().toArray(result);
		
		Booking[] expectedResult = new Booking[expectedList.length + 1];
		for(int i = 0; i < expectedList.length; i++)
			expectedResult[i] = expectedList[i];
		expectedResult[expectedList.length] = newBooking;
		
		assertArrayEquals(expectedResult, result);
		
		verify(displayBooking).printInfo(anyString(), anyString(), anyObject());
	}
	
	private Object[] paramsForBookRoomTest() {
		return new Object[] {
			new Object[] {
				1, 
				new boolean[][] {
					new boolean[] { true, true, true }
				},
				new Booking[] { b1 },
				new Booking[] { b1 }
			}
		};
	}
	
	//To test if user cancel booking and remove from its waiting list
	//u4
	@Test
	@Parameters(method = "paramsForCancelRoomTest")
	public void cancelBookingTest(boolean isWithinList, Booking[] initialList, 
			Booking targetBooking, Booking[] expectedList) {
		WaitingList wl = mock(WaitingList.class);
		Room allRooms = mock(Room.class);
		
		when(wl.getWaiting(anyObject())).thenReturn(isWithinList);
		
		ur.setAllBooking(initialList);
		
		ur.cancelBooking(wl, allRooms, targetBooking);
		
		Booking[] result = new Booking[ur.getAllBooking().size()];
		result = ur.getAllBooking().toArray(result);
		
		assertArrayEquals(expectedList, result);
	}
	
	private Object[] paramsForCancelRoomTest() {
		return new Object[] {
			new Object[] {
				false, 
				new Booking[] { b1, b2, b3 },
				b2,
				new Booking[] { b1, b3 }
			}
		};
	}
}
