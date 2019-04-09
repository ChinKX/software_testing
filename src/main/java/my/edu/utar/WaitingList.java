package my.edu.utar;

import java.util.ArrayList;

public class WaitingList {
	// instance method
	private ArrayList<User> vip;
	private ArrayList<User> member;
	private ArrayList<User> normal;
	
	// constructors
	public WaitingList() {
		vip = new ArrayList<User>();
		member = new ArrayList<User>();
		normal = new ArrayList<User>();
	}

	// instance method
	public void addWaiting(User user)
	{
		switch(user.get_member_type()) {
			case "vip":
				vip.add(user);
				break;
			case "member":
				member.add(user);
				break;
			case "normal":
				normal.add(user);
				break;
		}
	}
	
	public boolean getWaiting(User user)
	{
		boolean hasUser = false;
		
		switch(user.get_member_type()) {
			case "vip":
				if(vip.contains(user))
					hasUser = true;
				break;
			case "member":
				if(member.contains(user))
					hasUser = true;
				break;
			case "normal":
				if(normal.contains(user))
					hasUser = true;
				break;
		}
		
		return hasUser;
	}
		
	public void removeWaiting(User user)
	{
		switch(user.get_member_type()) {
			case "vip":
				vip.remove(user);
				break;
			case "member":
				member.remove(user);
				break;
			case "normal":
				normal.remove(user);
				break;
		}
	}
}
