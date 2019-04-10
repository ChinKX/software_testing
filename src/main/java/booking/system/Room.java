package booking.system;
public class Room {
	// instance variable
	// number of rooms available
	private static int vip;
	private static int deluxe;
	private static int standard;
	
	// instance method
	// check the availability of the selected room type
	public boolean checkRoom(String room_type, int requiredRoom)	{
		boolean booked = false;
		
		// check room type
		switch(room_type)
		{
			case "vip":
				if(vip > requiredRoom)
					booked = true;
				break;
			case "deluxe":
				if(deluxe > requiredRoom)
					booked = true;
				break;
			case "standard":
				if(standard > requiredRoom)
					booked = true;
				break;
		}
		
		return booked;
	}
	
	public void updateRoom(int vipRoom, int deluxeRoom, int standardRoom) {
		// update the number of room
	}
}
