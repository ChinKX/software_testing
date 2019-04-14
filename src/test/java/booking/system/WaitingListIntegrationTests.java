package Booking.test;

import Booking.system.*;

import java.util.ArrayList;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class WaitingListIntegrationTests {
	
	User user1= new User("ABC", "vip", false);
	User user2= new User("ABC", "member", false);
	User user3= new User("ABC", "nonMember", false);
	
	// set all list , each user's record is added in separate data set based on their privilege/class (vip,member or non-member)
	@Test
	@Parameters(method ="paramsForsetAllListTest")
	public void setAllListTest(User[] allUsers, User[] expectedVIP, User[] expectedMember,User[] expectedNonMember)
	{	
		WaitingList waiting = new WaitingList();
		waiting.setAllList(allUsers);
		
		User[] vipResult = new User[waiting.getVip().size()];
		vipResult = waiting.getVip().toArray(vipResult);
		
		User[] memberResult = new User[waiting.getMember().size()];
		memberResult = waiting.getMember().toArray(memberResult);

		User[] nonMemberResult = new User[waiting.getNonMember().size()];
		nonMemberResult = waiting.getNonMember().toArray(nonMemberResult);

		
		assertArrayEquals(vipResult,expectedVIP);
		assertArrayEquals(memberResult,expectedMember);
		assertArrayEquals(nonMemberResult,expectedNonMember);		
	}
	

	public Object[] paramsForsetAllListTest() {
		return new Object[] {	
				new Object[]{new User[]{user1,user2,user3}, new User[] {user1},new User[] {user2},new User[] {user3}}
		};
	}
	
	// to check if the system has added the correct user to the correct waiting list if the user fail to books the rooms he wanted
	@Test
	@Parameters(method ="paramsForAddWaitingTest")
	public void addWaitingTest (User addUser)
	{
		WaitingList waiting = new WaitingList();
		waiting.addWaiting(addUser);
		boolean vipAdded = false ,memberAdded = false, nonMemberAdded = false;
		ArrayList<User> vipResult,memberResult,nonMemberResult;
		
		// take three list and check each list
		
		vipResult =  waiting.getVip();
		memberResult =  waiting.getMember();
		nonMemberResult =  waiting.getNonMember();
		
		if (vipResult.contains(addUser))
		{
			vipAdded=true;
			assertTrue(vipAdded);
		}
		if (memberResult.contains(addUser))
		{
			memberAdded=true;
			assertTrue(memberAdded);
		}
		if (nonMemberResult.contains(addUser))
		{
			nonMemberAdded=true;
			assertTrue(nonMemberAdded);
		}	
	}
	
	public Object[] paramsForAddWaitingTest() {
		return new Object[] {	
				new Object[]{user1},
				new Object[]{user2},
				new Object[]{user3}
		};
	}
	
	// to check if the system stored the correct information of user in the correct waiting list
	@Test
	@Parameters(method ="paramsForGetWaitingTest")
	public void getWaitingTest (User user , boolean expectedResult)
	{
		WaitingList waiting = new WaitingList();
		waiting.addWaiting(user);
		boolean actualResult=waiting.getWaiting(user);
		
		assertEquals(actualResult,expectedResult);
	}
	
	public Object[] paramsForGetWaitingTest() {
		return new Object[] {	
				new Object[]{user1,true},
				new Object[]{user2,true},
				new Object[]{user3,true}
		};
	}
	
	// to check if the system remove the user's record correctly when the user asked for cancel booking 
	@Test
	@Parameters(method ="paramsForRemoveWaitingTest")
	public void removeWaitingTest (User removedUser , boolean expectedResult)
	{
		WaitingList waiting = new WaitingList();
		boolean isUserFound = false;
		boolean vipRemoved = false ,memberRemoved= false, nonMemberRemoved = false;
		ArrayList<User> vipResult,memberResult,nonMemberResult;
		
		waiting.addWaiting(removedUser);
		isUserFound=waiting.getWaiting(removedUser);
		
		
		if (isUserFound)
			waiting.removeWaiting(removedUser);
		
		// take three list and check each list
		vipResult =  waiting.getVip();
		memberResult =  waiting.getMember();
		nonMemberResult =  waiting.getNonMember();
		
		if (!vipResult.contains(removedUser))
		{
			vipRemoved=true;
			assertTrue(vipRemoved);
		}
		if (!memberResult.contains(removedUser))
		{
			memberRemoved=true;
			assertTrue(memberRemoved);
		}
		if (!nonMemberResult.contains(removedUser))
		{
			nonMemberRemoved=true;
			assertTrue(nonMemberRemoved);
		}
	}
	
	public Object[] paramsForRemoveWaitingTest() {
		return new Object[] {	
				new Object[]{user1,true},
				new Object[]{user2,true},
				new Object[]{user3,true}
		};
	}
	
}
