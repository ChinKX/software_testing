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

@RunWith(JUnitParamsRunner.class)
public class UserIntegrationTests {
	Scanner inputDataScanner;
	
	User user = new User("ABC", "vip", false);
	
	Booking b1 = new Booking(user);
	Booking b2 = new Booking(user);
	Booking b3 = new Booking(user);
	
	User vipUser = new User("randomVip", "vip", false);
	User memberUser = new User("randomMember", "member", false);
	User nonMemberUser = new User("randomPerson", "nonMember", false);
	
	// To book a room :
	/*
	 * 1) User books room 
	 * 2) System runs the book room use case (refer to booking integration test)
	 * 3) User books room successfully or he is added into the waiting list if all wanted rooms are fully booked
	 */
	
	@Test
	@Parameters(method = "paramsForBookRoomSuccessIntegrationTest")
	public void bookRoomSuccessIntegrationTest(String member_type , int numOfRoomsBooked, 
			boolean[][] IsRoomAvailable, Booking[] initialList,
			Booking[] expectedList, int[] expectedRoom, boolean excl_reward) {
		user.set_member_type(member_type);
		user.set_excl_reward(excl_reward);
		
		Booking newBooking = new Booking(user);
		WaitingList wl = new WaitingList();
		
		Printer displayBooking = mock(Printer.class);
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
		
		user.setAllBooking(initialList);
		
		user.bookRoom(displayBooking, wl, allRooms, newBooking, numOfRoomsBooked);
		
		Booking[] result = new Booking[user.getAllBooking().size()];
		result = user.getAllBooking().toArray(result);
		
		Booking[] expectedResult = new Booking[expectedList.length + 1];
		for(int i = 0; i < expectedList.length; i++)
			expectedResult[i] = expectedList[i];
		expectedResult[expectedList.length] = newBooking;
		
		assertArrayEquals(expectedResult, result);
		
		verify(allRooms).updateRoom(expectedRoom[0], expectedRoom[1], expectedRoom[2]);
		verify(displayBooking).printInfo(anyString(), anyString(), anyObject());
	}
	
	private Object[] paramsForBookRoomSuccessIntegrationTest() {
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
			
			Booking[] initialList = new Booking[] { b1, b2, b3 };
			param.add(initialList);// initialList
			
			Booking[] expectedList = new Booking[] { b1, b2, b3 };
			param.add(expectedList);// expectedList
			
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
	
	@Test
	@Parameters(method = "paramsForBookRoomFailIntegrationTest")
	public void bookRoomFailIntegrationTest(String member_type , int numOfRoomsBooked, 
			boolean[][] IsRoomAvailable, Booking[] initialList, 
			Booking[] expectedList, boolean expectedRewardChange) {
		user.set_member_type(member_type);
		user.set_excl_reward(expectedRewardChange);
		
		Booking newBooking = new Booking(user);
		WaitingList wl = new WaitingList();
		
		Printer displayBooking = mock(Printer.class);
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
		
		user.setAllBooking(initialList);
		
		user.bookRoom(displayBooking, wl, allRooms, newBooking, numOfRoomsBooked);
		
		Booking[] result = new Booking[user.getAllBooking().size()];
		result = user.getAllBooking().toArray(result);
		
		Booking[] expectedResult = new Booking[expectedList.length + 1];
		for(int i = 0; i < expectedList.length; i++)
			expectedResult[i] = expectedList[i];
		expectedResult[expectedList.length] = newBooking;
		
		assertArrayEquals(expectedResult, result);
	
		if(expectedRewardChange)
			assertEquals(user.get_excl_reward(), expectedRewardChange);
		
		verify(displayBooking).printInfo(anyString(), anyString(), anyObject());
	}
	
	private Object[] paramsForBookRoomFailIntegrationTest() {
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
			
			
			Booking[] initialList = new Booking[] { b1, b2, b3 };
			param.add(initialList);// initialList
			
			Booking[] expectedList = new Booking[] { b1, b2, b3 };
			param.add(expectedList);// expectedList
			
			param.add(Boolean.parseBoolean(line[3]));// expectedRewardChange
			
			params.add(param.toArray());
		}
		inputDataScanner.close();
		
		return params.toArray();
	}

	// To cancel a booking order :
	/*
	 * 1) User cancels booking 
	 * 2) System runs the cancel room use case (refer to booking integration test)
	 * 3) User is removed from waiting list and all wanted rooms are updated or vice versa
	 */
	@Test
	@Parameters(method = "paramsForCancelRoomIntegrationTest")
	public void cancelRoomIntegrationTest(boolean isWithinList, boolean used_reward, 
			Booking[] initialList, Booking targetBooking, Booking[] expectedList, 
			User currentUser, User[] allWaitingUsers, User[][] latestWaitingList) {		
		
		
		WaitingList wl = new WaitingList();
		Room allRooms = mock(Room.class);

		currentUser.setAllBooking(initialList);
		wl.setAllList(allWaitingUsers);
		if(isWithinList)
			wl.setAllList(new User[] { currentUser });
		
		currentUser.cancelBooking(wl, allRooms, targetBooking);
		
		Booking[] result = new Booking[currentUser.getAllBooking().size()];
		result = currentUser.getAllBooking().toArray(result);
		
		assertArrayEquals(expectedList, result);
		
		if(isWithinList)
		{
			// check whether the booking is removed
			assertArrayEquals(expectedList, currentUser.getAllBooking().toArray(expectedList));
			
			// check whether the system remove user from the correct list
			assertArrayEquals(latestWaitingList[0], wl.getVip().toArray(latestWaitingList[0]));
			assertArrayEquals(latestWaitingList[1], wl.getMember().toArray(latestWaitingList[1]));
			assertArrayEquals(latestWaitingList[2], wl.getNonMember().toArray(latestWaitingList[2]));	
		}
		else
		{
			if(used_reward)
				assertTrue(currentUser.get_excl_reward());
			verify(allRooms).updateRoom(anyInt(), anyInt(), anyInt());
		}
	}
	
	private Object[] paramsForCancelRoomIntegrationTest() {	
		ArrayList<Object> params = new ArrayList<Object>();

		String fileName = "paramsForCancelBookingTest.txt";
	
		try {
			inputDataScanner = new Scanner(new File(fileName));
		} catch(FileNotFoundException e) {
			System.out.println("Error opening the file!!!");
			System.exit(0);
		}

		while(inputDataScanner.hasNextLine()) {
			ArrayList<Object> param = new ArrayList<Object>();
			
			// split variable
			String[] line = inputDataScanner.nextLine().split(",");
			
			param.add(Boolean.parseBoolean(line[0]));// isWithinList
			param.add(Boolean.parseBoolean(line[1]));// usedExclReward
			//param.add(line[2]);// member_type
			
			Booking[] initialList = new Booking[] { b1, b2, b3 };
			param.add(initialList);// initialList
			
			param.add(b2); // targetedBooking
			
			Booking[] expectedList = new Booking[] { b1, b3 };
			param.add(expectedList);// expectedList
			
			User currentUser = new User("ABC", line[2], Boolean.parseBoolean(line[1]));
			param.add(currentUser); // currentUser
			
			User[] allWaitingUsers = new User[] { vipUser, memberUser, nonMemberUser };
			param.add(allWaitingUsers); // allWaitingUser
			
			User[][] latestWaitingList = new User[][] {
				new User[] { vipUser },
				new User[] { memberUser },
				new User[] { nonMemberUser }
			};
			param.add(latestWaitingList); // latestWaitingList
			
			params.add(param.toArray());
		}
		inputDataScanner.close();
		
		return params.toArray();
	}

	@Test(expected = IllegalArgumentException.class)
	@Parameters (method = "InvalidParamsforNumberOfRoomBookedIntegration")
	public void exceedBoookingNumberIntegrationTest(int numOfRoomsBooked, String member_type) {
		user.set_member_type(member_type);
		
		Booking newBooking = new Booking(user);	
		WaitingList wl = new WaitingList();
		
		Printer displayBooking = mock(Printer.class);
		Room allRooms = mock(Room.class);

		user.bookRoom(displayBooking, wl, allRooms, newBooking, numOfRoomsBooked);
	}
	
	private Object[] InvalidParamsforNumberOfRoomBookedIntegration() {
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
		user.set_excl_reward(excl_reward);
		user.set_member_type(member_type);
		
		Booking newBooking = new Booking(user);
		WaitingList wl = new WaitingList();
		
		Printer displayBooking = mock(Printer.class);
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

		user.bookRoom(displayBooking, wl, allRooms, newBooking, numOfRoomsBooked);
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

