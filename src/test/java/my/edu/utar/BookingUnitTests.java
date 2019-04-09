package my.edu.utar;

import java.util.ArrayList;
import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class BookingUnitTests {
	
	@Test
	@Parameters(method = "paramsForSetBookingTest")
	public void setBookingTest(String member_type, boolean excl_reward, 
			int numOfRoomsBooked, boolean[][] IsRoomAvailable, int[] expectedRoom,
			int numOfAddWaiting, int numOfUpdateRoom) {
		WaitingList wl = mock(WaitingList.class);
		Room allRooms = mock(Room.class);
		User ur = mock(User.class);
		
		Booking newBooking = new Booking(ur);
		
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
		
		when(ur.get_excl_reward()).thenReturn(excl_reward);
		when(ur.get_member_type()).thenReturn(member_type);
		
		newBooking.setBooking(wl, allRooms, ur, numOfRoomsBooked);
		
		verify(wl, times(numOfAddWaiting)).addWaiting(anyObject());
		verify(allRooms, times(numOfUpdateRoom)).updateRoom(expectedRoom[0], expectedRoom[1], expectedRoom[2]);
	}
	
	private Object[] paramsForSetBookingTest() {
		return new Object[] {
			new Object[] {
				"vip", false, 1,
				new boolean[][] {
					new boolean[] { true, true, true }
				},
				new int[] { -1, 0, 0 },
				0, 1
			},
			new Object[] {
					"vip", false, 2, 
				new boolean[][] {
					new boolean[] { true, true, true },
					new boolean[] { true, true, true }
				},
				new int[] { -2, 0, 0 },
				0, 1
			},
			new Object[] {
					"vip", true, 2,
				new boolean[][] {
					new boolean[] { true, true, true },
					new boolean[] { false, true, true }
				},
				new int[] { -1, -1, 0 },
				0, 1
			},
			new Object[] {
					"vip", true, 3,
				new boolean[][] {
					new boolean[] { true, true, true },
					new boolean[] { false, true, true },
					new boolean[] { false, false, true }
				},
				new int[] { -1, -1, -1 },
				0, 1
			},
			new Object[] {
				"member", false, 2,
				new boolean[][] {
					new boolean[] { true, true, true },
					new boolean[] { false, true, true }
				},
				new int[] { 0, -2, 0 },
				0, 1
			},
			new Object[] {
				"nonMember", false, 1,
				new boolean[][] {
					new boolean[] { true, true, true },
					new boolean[] { false, true, true },
					new boolean[] { false, false, true }
				},
				new int[] { 0, 0, -1 },
				0, 1
			}
		};
	}
}
