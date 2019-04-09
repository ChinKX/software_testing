package my.edu.utar;

import java.util.ArrayList;

public class User {
	// instance variables
	private String name;
	private String member_type;
	private boolean excl_reward;
	private ArrayList<Booking> allBooking;
	
	public User(String name, String member_type, boolean excl_reward) {
		this.name = name;
		this.member_type = member_type;
		this.excl_reward = excl_reward;
		allBooking = new ArrayList<Booking>();
	}
	
    // instance method
	public String get_member_type()	{
		return member_type;
	}
	
	public boolean get_excl_reward() {
		return excl_reward;
	}
	
	public void set_excl_reward(boolean change) {
		excl_reward = change;
	}
	
	public void bookRoom(WaitingList waitingList, Room allRooms, int numOfRoomsBooked)	{
		Booking newBooking = new Booking();
		newBooking.setBooking(waitingList, allRooms, this, numOfRoomsBooked);
		allBooking.add(newBooking);
	}
	
	public void cancelBooking(WaitingList waitingList, Room allRooms, Booking targetBooking) {
		targetBooking.cancelBooking(waitingList, allRooms, this);
		allBooking.remove(targetBooking);
	}
}
