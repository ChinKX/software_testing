package Booking.system;
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
	public void setAllBooking(Booking[] allBooking) {
		for(Booking b : allBooking)
			this.allBooking.add(b);
	}
	
	public ArrayList<Booking> getAllBooking() {
		return allBooking;
	}
	
	public void set_member_type(String member_type) {
		this.member_type = member_type;
	}
	
	public String get_member_type()	{
		return member_type;
	}
	
	public boolean get_excl_reward()	{
		return excl_reward;
	}
	
	public void set_excl_reward(boolean change) {
		excl_reward = change;
	}
	
	public void bookRoom(Printer displayBooking, WaitingList waitingList, Room allRooms, Booking newBooking, int numOfRoomsBooked)	{
		newBooking.setBooking(waitingList, allRooms, this, numOfRoomsBooked);
		allBooking.add(newBooking);
		
		displayBooking.printInfo(name, member_type, 
				newBooking.getVipRoom() + " VIP, " + 
				newBooking.getDeluxeRoom() + " Deluxe, " + 
				newBooking.getStandardRoom() + " Standard");
	}
	
	public void cancelBooking(WaitingList waitingList, Room allRooms, Booking targetBooking) {
		targetBooking.cancelBooking(waitingList, allRooms, this);
		allBooking.remove(targetBooking);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!member_type.equals(other.get_member_type()))
			return false;
		if (excl_reward != other.get_excl_reward())
			return false;
		return true;
	}
}
