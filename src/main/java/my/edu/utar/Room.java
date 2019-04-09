package my.edu.utar;

public class Room {
	// instance variable
	// number of rooms available
	private static int vip;
	private static int deluxe;
	private static int standard;
	
	// instance method
	// check the availability of the selected room type
	public boolean checkRoom(String room_type)	{
		boolean available = false;
		
		// check room type
		switch(room_type)
		{
			case "vip":
				if(vip > 0)
					available = true;
				break;
			case "deluxe":
				if(deluxe > 0)
					available = true;
				break;
			case "standard":
				if(standard > 0)
					available = true;
				break;
		}
		
		return available;
	}
	
	public void updateRoom(int vipRoom, int deluxeRoom, int standardRoom) {
		// update the number of room
	}
}
