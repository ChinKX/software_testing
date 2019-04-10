package booking.system;

import static java.lang.System.out;

public class Printer {
	
	public void printInfo(String name, String member_type, Booking booking)
	{
		out.println("User name: " + name);
		out.println("Member Type: " + member_type);
		
		int vipRoom = booking.getVipRoom();
		int deluxeRoom = booking.getDeluxeRoom();
		int standardRoom = booking.getStandardRoom();
		
		if(vipRoom != 0 && deluxeRoom != 0 && standardRoom != 0)
			out.print("Currently, this user is in waiting list");
		else
		{
			out.print("Room Type: ");
			if(vipRoom > 0)
				out.print(vipRoom + " VIP Room ");
			if(deluxeRoom > 0)
				out.print(deluxeRoom + " Deluxe Room ");
			if(standardRoom > 0)
				out.print(standardRoom + " Standard Room ");
		}
	}
}
