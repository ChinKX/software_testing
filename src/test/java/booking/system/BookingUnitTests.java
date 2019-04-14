package Booking.test;

import Booking.system.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
// this unit tests are using decision table techniques
@RunWith(JUnitParamsRunner.class)
public class BookingUnitTests {
	
	Scanner inputDataScanner;
	
	//get user
	@Test
	public void getOwnerTest() {
		User ur = mock(User.class);
		Booking aBooking = new Booking(ur);
		
		assertEquals(ur, aBooking.getOwner());
	}
	
	@Test
	@Parameters({
		"true, true",
		"false, false"
	})
	
	//get user exclusive reward
	public void getSetUsedExclReward(boolean used_excl_reward, boolean expectedResult) {
		User ur = mock(User.class);
		Booking aBooking = new Booking(ur);
		
		aBooking.set_used_excl_reward(used_excl_reward);
		boolean result = aBooking.get_used_excl_reward();
		
		assertEquals(expectedResult, result);
	}
	
	//User successfully books rooms
	private Object[] paramsForSetBookingSuccessTest() {
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
				expectedRoom[i] = Integer.parseInt(_expectedRoom[i]);
				//System.out.println(expectedRoom[i]);
			}
			param.add(expectedRoom);// expectedRoom
			
			//System.out.println(String.valueOf(line[4]));
			param.add(Boolean.parseBoolean(line[4]));// excl_reward
			
			params.add(param.toArray());
		}
		inputDataScanner.close();
		
		return params.toArray();
	}
	
	//User successfully books rooms
	@Test
	@Parameters(method = "paramsForSetBookingSuccessTest")
	public void setBookingSuccessTest(String member_type , int numOfRoomsBooked, 
			boolean[][] IsRoomAvailable, int[] expectedRoom, boolean excl_reward) {
		
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
		
		when(ur.get_excl_reward()).thenAnswer(new Answer() {
			boolean usedReward = false;
			
			public Object answer(InvocationOnMock invocation) {
				if(excl_reward)
					if(!usedReward) {
						usedReward = true;
						return true;
					}
						
				return false;
			}
		});
		
		when(ur.get_member_type()).thenReturn(member_type);
		
		newBooking.setBooking(wl, allRooms, ur, numOfRoomsBooked);
		
		verify(allRooms).updateRoom(expectedRoom[0], expectedRoom[1], expectedRoom[2]);
	}
	
	private Object[] paramsForSetBookingFailTest() {
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
	
	@Test
	@Parameters(method = "paramsForSetBookingFailTest")
	public void setBookingFailTest(String member_type, int numOfRoomsBooked, 
			boolean[][] IsRoomAvailable, boolean expectedRewardChange) {
		
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
		
		when(ur.get_member_type()).thenReturn(member_type);

		when(ur.get_excl_reward()).thenAnswer(new Answer() {
			boolean usedReward = false;
			
			public Object answer(InvocationOnMock invocation) {
				if(expectedRewardChange)
					if(!usedReward)
					{
						usedReward = true;
						return true;
					}
				
				return false;
			}
		});
		
		newBooking.setBooking(wl, allRooms, ur, numOfRoomsBooked);

		verify(wl).addWaiting(anyObject());
		
		if(expectedRewardChange)
			verify(ur).set_excl_reward(expectedRewardChange);
	}
	
	private Object[] paramsForCancelBookingTest() {
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
	
	//User fails to book rooms and registered into waiting list
	@Test
	@Parameters(method = "paramsForCancelBookingTest")
	public void cancelBookingTest(boolean isWithinList, boolean used_reward, String member_type) {
		WaitingList wl = mock(WaitingList.class);
		Room allRooms = mock(Room.class);
		User ur = mock(User.class);
		
		Booking booking = new Booking(ur);
		
		when(wl.getWaiting(anyObject())).thenReturn(isWithinList);
		when(ur.get_member_type()).thenReturn(member_type);
		
		booking.set_used_excl_reward(used_reward);
		booking.cancelBooking(wl, allRooms, ur);
		
		if(isWithinList)
			verify(wl).removeWaiting(ur);
		else
		{
			if(used_reward)
				verify(ur).set_excl_reward(true);
			verify(allRooms).updateRoom(anyInt(), anyInt(), anyInt());
		}
	}
	
	private Object[] InvalidParamsforNumberOfRoomBooked() {
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
	
	// when user books invalid number of rooms ,either exceed the limit he/she can book or the system receive an empty booking order
	@Test(expected = IllegalArgumentException.class)
	@Parameters (method = "InvalidParamsforNumberOfRoomBooked")
	public void exceedBoookingNumberTest(int numOfRoomsBooked, String member_type) {
		
		WaitingList wl = mock(WaitingList.class);
		Room allRooms = mock(Room.class);
		User ur = mock(User.class);
		
		Booking booking = new Booking(ur);
		
		when(ur.get_member_type()).thenReturn(member_type);
		booking.setBooking(wl, allRooms, ur, numOfRoomsBooked);
	}
	
	private Object[] InvalidParamsforBookingTest() {
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
	
	// To test invalid booking methods and combinations so as the invalid system behavior in updating the exclusive rewards of one
	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "InvalidParamsforBookingTest")
	public void invalidBookingCombinationTest(String member_type, int numOfRoomsBooked, 
				boolean[][] IsRoomAvailable, boolean excl_reward) {
			
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
		
		when(ur.get_excl_reward()).thenAnswer(new Answer() {
			boolean usedReward = false;
			
			public Object answer(InvocationOnMock invocation) {
				if(excl_reward)
					if(!usedReward) {
						usedReward = true;
						return true;
					}
				
				return false;
			}
		});
		when(ur.get_member_type()).thenReturn(member_type);
		
		newBooking.setBooking(wl, allRooms, ur, numOfRoomsBooked);
	}
}
