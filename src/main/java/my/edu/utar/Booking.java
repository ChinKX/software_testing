package my.edu.utar;

// this class will represent the whole booking system
public class Booking {
	// instance variable
	private int vipRoom, deluxeRoom, standardRoom;
	private boolean used_excl_reward;
	
	// constructor
	public Booking() {
		vipRoom = 0;
		deluxeRoom = 0;
		standardRoom = 0;
		used_excl_reward = false;
	}

	// instance method
	public void setBooking(WaitingList waitingList, Room allRooms, User user, int numOfRoomsBooked)
	{
		String member_type = user.get_member_type();
		
		boolean bookingFail = false;
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
					availableVip = allRooms.checkRoom("vip");
					availableDeluxe = allRooms.checkRoom("deluxe");
					maximum_booking++;
				case "normal":
					availableStandard = allRooms.checkRoom("standard");
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
					if (user.get_excl_reward() && availableVip)
					{
						vipRoom++;
						user.set_excl_reward(false);
						used_excl_reward = true;
					}
					else if (availableDeluxe)
						deluxeRoom++;
					else if (availableStandard)
						standardRoom++;
					else
						bookingFail = true;
					break;
				case "normal":
					if (availableStandard)
						standardRoom++;
					else
						bookingFail = true;
					break;
			}
		}
		
		// booking process
		if(bookingFail)
			waitingList.addWaiting(user);
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
}
