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
public class UserUnitTests {
	User ur = new User("Apple", "vip", false);
	
	User u1 = new User("abc", "vip", false);
	User u2 = new User("Tester", "nonMember", false);
	User u3 = new User("cat", "member", true);
	
	Booking newBooking;
	Booking b1 = new Booking(u2);
	Booking b2 = new Booking(u1);
	Booking b3 = new Booking(u1);
	Booking b4 = new Booking(u3);
	Booking b5 = new Booking(u3);
	
	@Before
	public void beforeClass() {
		 newBooking = new Booking(ur);
	}
	
	@Test
	@Parameters(method = "paramsForBookRoomTest")
	public void bookRoomTest(int numOfRoomsBooked, boolean[][] IsRoomAvailable, int[] expectedRoom) {
		WaitingList wl = mock(WaitingList.class);
		Room allRooms = mock(Room.class);
		for(int i = 0; i < IsRoomAvailable.length; i++)
		{
			when(allRooms.checkRoom("vip", i)).thenReturn(IsRoomAvailable[i][0]);
			when(allRooms.checkRoom("member", i)).thenReturn(IsRoomAvailable[i][1]);
			when(allRooms.checkRoom("nonMember", i)).thenReturn(IsRoomAvailable[i][2]);
		}
		
		ur.bookRoom(wl, allRooms, newBooking, numOfRoomsBooked);
		
		//Booking[] result = new Booking[ur.getAllBooking().size()];
		//result = ur.getAllBooking().toArray(result);
		
		verify(allRooms).updateRoom(expectedRoom[0], expectedRoom[1], expectedRoom[2]);
	}
	
	private Object[] paramsForBookRoomTest() {
		return new Object[] {
			new Object[] {
				1, 
				new boolean[][] {
						new boolean[] { true, true, true }
				},
				new int[] { -1, 0, 0 }
			},
			new Object[] {
				2, 
				new boolean[][] {
					new boolean[] { true, true, true },
					new boolean[] { true, true, true }
				},
				new int[] { -2, 0, 0 }
			},
			new Object[] {
				2, 
				new boolean[][] {
					new boolean[] { true, true, true },
					new boolean[] { false, true, true }
				},
				new int[] { -1, -1, 0 }
			},
			new Object[] {
				3, 
				new boolean[][] {
					new boolean[] { true, true, true },
					new boolean[] { false, true, true },
					new boolean[] { false, false, true }
				},
				new int[] { -1, -1, -1 }
			}
		};
	}
}
