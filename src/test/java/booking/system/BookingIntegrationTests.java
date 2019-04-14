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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

@RunWith(JUnitParamsRunner.class)
public class BookingIntegrationTests {
	Scanner inputDataScanner;
	
	// To book a room :
	/*
	 * 1) User books room 
	 * 2) System creates a booking record of user
	 * 3) User inputs the number of rooms he want to book limited to his privilege  (vip,member,non member)
	 * 4) System checks if rooms are available
	 * 5) System checks if the user has exclusive reward , then upgrades his room's standard automatically to vip
	 * 6) User successfully books rooms
	 * 
	 * alternatives:
	 * 6) If all rooms are fully booked , user's record is added into the particular waiting list while his exclusive reward unchanged
	 */
	
	@Test
	@Parameters(method = "paramsForSetBookingSuccessIntegrationTest")
	public void setBookingSuccessIntegrationTest(String member_type , int numOfRoomsBooked, 
			boolean[][] IsRoomAvailable, int[] expectedRoomResult, boolean excl_reward) {
		
		WaitingList wl = new WaitingList();
		Room allRooms = mock(Room.class);
		User ur = new User("RandomName", member_type, excl_reward);
		
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
		
		newBooking.setBooking(wl, allRooms, ur, numOfRoomsBooked);
		
		assertEquals(expectedRoomResult[0], newBooking.getVipRoom());
		assertEquals(expectedRoomResult[1], newBooking.getDeluxeRoom());
		assertEquals(expectedRoomResult[2], newBooking.getStandardRoom());
	}
	
	private Object[] paramsForSetBookingSuccessIntegrationTest() {
		String fileName = "paramsForSetBookingSuccessTest.txt";
		
		try {
			inputDataScanner = new Scanner(new File(fileName));
		} catch(FileNotFoundException e) {
			System.out.println("Error opening the file!!!");
			System.exit(0);
		}
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		while(inputDataScanner.hasNextLine()) {
			ArrayList<Object> param = new ArrayList<Object>();
			
			// split variable
			String[] line = inputDataScanner.nextLine().split(",");
			
			param.add(line[0]);// member_type
			param.add(Integer.parseInt(line[1]));// numOfRoomsBooked
			
			String[] allRoomAvailability = String.valueOf(line[2]).split(":");
			boolean[][] eachAvailability = new boolean[allRoomAvailability.length][3];
			for(int x = 0; x < allRoomAvailability.length; x++)
			{	
				String[] eachIteration = allRoomAvailability[x].split(" ");
				for (int y = 0; y < 3; y++)
					eachAvailability[x][y] = Boolean.parseBoolean(eachIteration[y]);
			}
			param.add(eachAvailability);// IsRoomAvailable
			
			int[] expectedRoom = new int[3];
			String[] _expectedRoom = String.valueOf(line[3]).split(" ");
			for (int i = 0; i < expectedRoom.length; i++) {
				expectedRoom[i] = -Integer.parseInt(_expectedRoom[i]);// -ve sign is put in front to convert -ve value to +ve value
			}
			param.add(expectedRoom);// expectedRoom
			
			param.add(Boolean.parseBoolean(line[4]));// excl_reward
			
			params.add(param.toArray());
		}
		inputDataScanner.close();
		
		return params.toArray();
	}
	
	@Test
	@Parameters(method = "paramsForSetBookingFailIntegrationTest")
	public void setBookingFailIntegrationTest(String member_type, int numOfRoomsBooked, 
			boolean[][] IsRoomAvailable, boolean expectedRewardChange) {
		
		WaitingList wl = new WaitingList();
		Room allRooms = mock(Room.class);
		User ur = new User("RandomName", member_type, expectedRewardChange);
		
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
		
		newBooking.setBooking(wl, allRooms, ur, numOfRoomsBooked);

		assertTrue(wl.getWaiting(ur));
		
		if(expectedRewardChange)
			assertTrue(ur.get_excl_reward());
	}
	
	private Object[] paramsForSetBookingFailIntegrationTest() {
		String fileName = "paramsForSetBookingFailTest.txt";
		
		try {
			inputDataScanner = new Scanner(new File(fileName));
		} catch(FileNotFoundException e) {
			System.out.println("Error opening the file!!!");
			System.exit(0);
		}
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		while(inputDataScanner.hasNextLine()) {
			ArrayList<Object> param = new ArrayList<Object>();
			
			// split variable
			String[] line = inputDataScanner.nextLine().split(",");
			
			param.add(line[0]);// member_type
			param.add(Integer.parseInt(line[1]));// numOfRoomsBooked
			
			String[] allRoomAvailability = String.valueOf(line[2]).split(":");
			boolean[][] eachAvailability = new boolean[allRoomAvailability.length][3];
			for(int x = 0; x < allRoomAvailability.length; x++)
			{	
				String[] eachIteration = allRoomAvailability[x].split(" ");
				for (int y = 0; y < 3; y++)
					eachAvailability[x][y] = Boolean.parseBoolean(eachIteration[y]);
			}
			param.add(eachAvailability);// IsRoomAvailable
			
			param.add(Boolean.parseBoolean(line[3]));// expectedRewardChange
			
			params.add(param.toArray());
		}
		inputDataScanner.close();
		
		return params.toArray();
	}
	
	// To cancel a booking order :
	/*
	 * 1) User cancels booking 
	 * 2) System finds user's record in the particular waiting list 
	 * 3) System removes user's record from the selected waiting list
	 * 4) availability of rooms are updated following the booking order
	 * 5) User cancels booking successfully
	 *
	 * 
	 * alternatives:
	 * 2) If system couldn't find user's record in the particular waiting list
	 * 3) system terminate the cancel booking function and availability of rooms are not updated
	 */
	
	@Test
	@Parameters(method = "paramsForCancelBookingIntegrationTest")
	public void cancelBookingIntegrationTest(boolean isWithinList, boolean used_reward, String member_type) {
		WaitingList wl = new WaitingList();
		Room allRooms = mock(Room.class);
		User ur = new User("RandomName", member_type, used_reward);
		
		Booking booking = new Booking(ur);
		
		booking.set_used_excl_reward(used_reward);
		booking.cancelBooking(wl, allRooms, ur);
		
		if(isWithinList)
			assertFalse(wl.getWaiting(ur));
		else
		{
			if(used_reward)
				assertTrue(ur.get_excl_reward());
			verify(allRooms).updateRoom(anyInt(), anyInt(), anyInt());
		}
	}
	
	private Object[] paramsForCancelBookingIntegrationTest() {
		String fileName = "paramsForCancelBookingTest.txt";
		
		try {
			inputDataScanner = new Scanner(new File(fileName));
		} catch(FileNotFoundException e) {
			System.out.println("Error opening the file!!!");
			System.exit(0);
		}
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		while(inputDataScanner.hasNextLine()) {
			ArrayList<Object> param = new ArrayList<Object>();
			
			// split variable
			String[] line = inputDataScanner.nextLine().split(",");
			
			param.add(Boolean.parseBoolean(line[0]));// isWithinList
			param.add(Boolean.parseBoolean(line[1]));// usedExclReward
			param.add(line[2]);// member_type
			
			params.add(param.toArray());
		}
		inputDataScanner.close();
		
		return params.toArray();
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Parameters (method = "paramsforExceedBookingNumberIntegration")
	public void exceedBoookingNumberIntegrationTest(int numOfRoomsBooked, String member_type) {

		User ur = new User("RandomName", member_type, false);

		Booking booking = new Booking(ur);
		WaitingList wl = new WaitingList();
		
		Room allRooms = mock(Room.class);
		
		booking.setBooking(wl, allRooms, ur, numOfRoomsBooked);
	}
	
	private Object[] paramsforExceedBookingNumberIntegration() {
		String fileName = "InvalidParamsforNumberOfRoomBooked.txt";
		
		try {
			inputDataScanner = new Scanner(new File(fileName));
		} catch(FileNotFoundException e) {
			System.out.println("Error opening the file!!!");
			System.exit(0);
		}
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		while(inputDataScanner.hasNextLine()) {
			ArrayList<Object> param = new ArrayList<Object>();
			
			// split variable
			String[] line = inputDataScanner.nextLine().split(",");
			
			param.add(Integer.parseInt(line[0]));// numOfRoomsBooked
			param.add(line[1]);// member_type
			
			params.add(param.toArray());
		}
		inputDataScanner.close();
		
		return params.toArray();
	}
	
	// to test invalid booking room methods and combinations
	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "InvalidParamsforBookingIntegrationTest")
	public void invalidBookingCombinationIntegrationTest(String member_type, int numOfRoomsBooked, 
				boolean[][] IsRoomAvailable, boolean excl_reward) {
			
			WaitingList wl = new WaitingList();
			Room allRooms = mock(Room.class);
			User ur = new User("RandomName", member_type, excl_reward);
			
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
			
			newBooking.setBooking(wl, allRooms, ur, numOfRoomsBooked);
	}
	
	private Object[] InvalidParamsforBookingIntegrationTest() {
		String fileName = "InvalidParamsforBookingTest.txt";
		
		try {
			inputDataScanner = new Scanner(new File(fileName));
		} catch(FileNotFoundException e) {
			System.out.println("Error opening the file!!!");
			System.exit(0);
		}
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		while(inputDataScanner.hasNextLine()) {
			ArrayList<Object> param = new ArrayList<Object>();
			
			// split variable
			String[] line = inputDataScanner.nextLine().split(",");
			
			param.add(line[0]);// member_type
			param.add(Integer.parseInt(line[1]));// numOfRoomsBooked
			
			String[] allRoomAvailability = String.valueOf(line[2]).split(":");
			boolean[][] eachAvailability = new boolean[allRoomAvailability.length][3];
			for(int x = 0; x < allRoomAvailability.length; x++)
			{	
				String[] eachIteration = allRoomAvailability[x].split(" ");
				for (int y = 0; y < 3; y++)
					eachAvailability[x][y] = Boolean.parseBoolean(eachIteration[y]);
			}
			param.add(eachAvailability);// IsRoomAvailable
			
			param.add(Boolean.parseBoolean(line[3]));// excl_reward
			
			params.add(param.toArray());
		}
		inputDataScanner.close();
		
		return params.toArray();
	}
}