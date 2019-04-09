package my.edu.utar;
// this class will represent the whole booking system
public class Booking {
	// instance variable
	private User owner;
	private int vipRoom, deluxeRoom, standardRoom;
	private boolean used_excl_reward;
	
	// constructor
	public Booking(User owner) {
		this.owner = owner;
		vipRoom = 0;
		deluxeRoom = 0;
		standardRoom = 0;
		used_excl_reward = false;
	}

	// instance method
	public User getOwner() {
		return owner;
	}
	
	public int getVipRoom() {
		return vipRoom;
	}
	
	public int getDeluxeRoom() {
		return deluxeRoom;
	}
	
	public int getStandardRoom() {
		return standardRoom;
	}
	
	public boolean get_used_excl_reward() {
		return used_excl_reward;
	}
	
	public void setBooking(WaitingList waitingList, Room allRooms, User user, int numOfRoomsBooked)
	{
		String member_type = user.get_member_type();
		
		boolean bookingFail = false, using_excl_reward = false;
		boolean availableVip = false, availableDeluxe = false, availableStandard = false;
		int maximum_booking;
		
		for(int i = 0; i < numOfRoomsBooked && !bookingFail; i++)
		{
			maximum_booking = 0;
			// check room availability and get maximum booking
			switch(member_type)	{
				case "vip":
					maximum_booking++;
				case "member":
					availableVip = allRooms.checkRoom("vip", vipRoom);
					availableDeluxe = allRooms.checkRoom("member", deluxeRoom);
					maximum_booking++;
				case "nonMember":
					availableStandard = allRooms.checkRoom("nonMember", standardRoom);
					maximum_booking++;
					break;
			}
			
			// check the booking limit
			if(numOfRoomsBooked > maximum_booking)
				throw new IllegalArgumentException();
			
			// allocation process
			switch(member_type) {
				case "vip":
					if (availableVip)
						vipRoom++;
					else if (availableDeluxe)
						deluxeRoom++;
					else if (availableStandard)
						standardRoom++;
					else
						bookingFail = true;
					break;
				case "member":
					if (availableDeluxe)
						deluxeRoom++;
					else if (user.get_excl_reward() && availableVip && using_excl_reward)
					{
						vipRoom++;
						using_excl_reward = true;
						
					}
					else if (availableStandard)
						standardRoom++;
					else
						bookingFail = true;
					break;
				case "nonMember":
					if (availableStandard)
						standardRoom++;
					else
						bookingFail = true;
					break;
			}
		}
		
		// booking process
		if(bookingFail)
		{
			waitingList.addWaiting(user);
			using_excl_reward = false;
		}
		else
			allRooms.updateRoom(-vipRoom, -deluxeRoom, -standardRoom);
	}
	
	public void cancelBooking(WaitingList waitingList, Room allRooms, User user)
	{
		if(waitingList.getWaiting(user))
			waitingList.removeWaiting(user);
		
		if(used_excl_reward)
		{
			used_excl_reward = false;
			user.set_excl_reward(true);
		}
		
		allRooms.updateRoom(vipRoom, deluxeRoom, standardRoom);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		if(!owner.equals(other.getOwner()))
			return false;
		if (vipRoom != other.getVipRoom())
			return false;
		else if(deluxeRoom != other.getDeluxeRoom())
			return false;
		else if (standardRoom != other.getStandardRoom())
			return false;
		else if (used_excl_reward != other.get_used_excl_reward())
			return false;
		return true;
	}
}
