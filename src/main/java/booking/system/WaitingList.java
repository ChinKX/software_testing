package booking.system;
import java.util.ArrayList;

public class WaitingList {
	// instance method
	private ArrayList<User> vip;
	private ArrayList<User> member;
	private ArrayList<User> nonMember;
	
	// constructors
	public WaitingList() {
		vip = new ArrayList<User>();
		member = new ArrayList<User>();
		nonMember = new ArrayList<User>();
	}
	
	// properties
	public ArrayList<User> getVip() {
		return vip;
	}
	
	public void setAllList(User[] allVips) {
		for(User user : allVips){
			switch(user.get_member_type())
			{
				case "vip": vip.add(user);
					break;
				case "member": member.add(user);
					break;
				case "nonMember": nonMember.add(user);
					break;
			}
		}
	}

	public ArrayList<User> getMember() {
		return member;
	}	

	public ArrayList<User> getNonMember() {
		return nonMember;
	}

	// instance method
	public void addWaiting(User user)
	{
		switch(user.get_member_type()) {
			case "vip":
				//if(!vip.contains(user))
					vip.add(user);
				break;
			case "member":
				//if(!member.contains(user))
					member.add(user);
				break;
			case "nonMember":
				//if(!nonMember.contains(user))
					nonMember.add(user);
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
			case "nonMember":
				if(nonMember.contains(user))
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
			case "nonMember":
				nonMember.remove(user);
				break;
		}
	}
}
