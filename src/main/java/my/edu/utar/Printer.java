package my.edu.utar;

import static java.lang.System.out;

public class Printer {
	
	public void printInfo(String name, String member_type, String room_type)
	{
		out.println("User name: " + name);
		out.println("Member Type: " + member_type);
		out.println("Room Type: " + room_type);
	}
}
