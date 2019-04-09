package my.edu.utar;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class WaitingListUnitTests {
	WaitingList wl;
	
	User u1 = new User("abc", "vip", false);
	User u2 = new User("Tester", "nonMember", false);
	User u3 = new User("cat", "member", true);
	
	@Before
	private void beforeClass() {
		wl = new WaitingList();
	}
	
	private Object[] paramsForPropertyTest() {
		return new Object[] {
			new Object[] {
					new User[] { u1 },
					new User[] { u1 }
			},
			new Object[] {
					new User[] { u2 },
					new User[] { u2 }
			},
			new Object[] {
					new User[] { u1, u2 },
					new User[] { u1, u2 }
			},
			new Object[] {
					new User[] { u3, u1, u2 },
					new User[] { u3, u1, u2 }
			},
			new Object[] {
					new User[] { u1, u3, u1, u2 },
					new User[] { u1, u3, u1, u2 }
			}
		};
	}
	
	@Test
	@Parameters(method = "paramsForPropertyTest")
	public void getSetVipTest(User[] initialList, User[] expectedResult) {
		wl.setVip(initialList);
		User[] result = new User[wl.getVip().size()];
		result = wl.getVip().toArray(result);
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	@Parameters(method = "paramsForPropertyTest")
	public void getSetMemberTest(User[] initialList, User[] expectedResult) {
		wl.setMember(initialList);
		User[] result = new User[wl.getMember().size()];
		result = wl.getMember().toArray(result);
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	@Parameters(method = "paramsForPropertyTest")
	public void getSetNonMemberTest(User[] initialList, User[] expectedResult) {
		wl.setNonMember(initialList);
		User[] result = new User[wl.getNonMember().size()];
		result = wl.getNonMember().toArray(result);
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	@Parameters(method = "paramsForAddWaitingTest")
	public void addWaitingTest(User[] initialList, User currentUser, User[] expectedResult) {
		switch(currentUser.get_member_type()) {
			case "vip":
				wl.setVip(initialList);
				break;
			case "member":
				wl.setMember(initialList);
				break;
			case "nonMember":
				wl.setNonMember(initialList);
				break;
		}
		
		wl.addWaiting(currentUser);
		
		User[] result;
		
		switch(currentUser.get_member_type()) {
			case "vip":
				result = new User[wl.getVip().size()];
				result = wl.getVip().toArray(result);
				assertArrayEquals(expectedResult, result);
				break;
			case "member":
				result = new User[wl.getMember().size()];
				result = wl.getMember().toArray(result);
				assertArrayEquals(expectedResult, result);
				break;
			case "nonMember":
				result = new User[wl.getNonMember().size()];
				result = wl.getNonMember().toArray(result);
				assertArrayEquals(expectedResult, result);
				break;
		}
	}
	
	private Object[] paramsForAddWaitingTest() {
		return new Object[] {
			new Object[] {
					new User[] { u1 },
					u1,
					new User[] { u1, u1 }
			},
			new Object[] {
					new User[] { u2 },
					u1,
					new User[] { u2, u1 }
			},
			new Object[] {
					new User[] { u1 },
					u2,
					new User[] { u1, u2 }
			},
			new Object[] {
					new User[] { u3, u1 },
					u2,
					new User[] { u3, u1, u2 }
			},
			new Object[] {
					new User[] { u1, u3, u1 },
					u2,
					new User[] { u1, u3, u1, u2 }
			}
		};
	}

	@Test
	@Parameters(method = "paramsForGetWaitingTest")
	public void getWaitingTest(User[] initialList, User currentUser, boolean expectedResult) {
		switch(currentUser.get_member_type()) {
			case "vip":
				wl.setVip(initialList);
				break;
			case "member":
				wl.setMember(initialList);
				break;
			case "nonMember":
				wl.setNonMember(initialList);
				break;
		}
		
		boolean result = wl.getWaiting(currentUser);
		
		assertEquals(expectedResult, result);
	}
	
	private Object[] paramsForGetWaitingTest() {
		return new Object[] {
			new Object[] {
					new User[] { u1, u1 },
					u1, true
			},
			new Object[] {
					new User[] { u2, u1 },
					u2, true
			},
			new Object[] {
					new User[] { u3, u2, u1 },
					u2, true
			},
			new Object[] {
					new User[] { u1, u3, u1, u2 },
					u3, true
			},
			new Object[] {
					new User[] {  },
					u1, false
			},
			new Object[] {
					new User[] { u1 },
					u2, false
			},
			new Object[] {
					new User[] { u3, u1 },
					u2, false
			},
			new Object[] {
					new User[] { u1, u2 },
					u3, false
			}
		};
	}
	
	@Test
	@Parameters(method = "paramsForRemoveWaitingTest")
	public void removeWaitingTest(User[] initialList, User currentUser, User[] expectedResult) {
		switch(currentUser.get_member_type()) {
			case "vip":
				wl.setVip(initialList);
				break;
			case "member":
				wl.setMember(initialList);
				break;
			case "nonMember":
				wl.setNonMember(initialList);
				break;
		}
		
		wl.removeWaiting(currentUser);
		
		User[] result;
		
		switch(currentUser.get_member_type()) {
			case "vip":
				result = new User[wl.getVip().size()];
				result = wl.getVip().toArray(result);
				assertArrayEquals("Wrong VIP", expectedResult, result);
				break;
			case "member":
				result = new User[wl.getMember().size()];
				result = wl.getMember().toArray(result);
				assertArrayEquals("Wrong Member", expectedResult, result);
				break;
			case "nonMember":
				result = new User[wl.getNonMember().size()];
				result = wl.getNonMember().toArray(result);
				assertArrayEquals("Wrong Non Member", expectedResult, result);
				break;
		}
	}

	private Object[] paramsForRemoveWaitingTest() {
		return new Object[] {
			new Object[] {
					new User[] { u1, u1 },
					u1,
					new User[] { u1 }
			},
			new Object[] {
					new User[] { u2, u1 },
					u1,
					new User[] { u2 }
			},
			new Object[] {
					new User[] { u2, u1 },
					u2,
					new User[] { u1 }
			},
			new Object[] {
					new User[] { u3, u2, u1 },
					u2,
					new User[] { u3, u1 }
			},
			new Object[] {
					new User[] { u1, u3, u1, u2 },
					u3,
					new User[] { u1, u1, u2 }
			},
			new Object[] {
					new User[] { u1, u3, u1, u2 },
					u1,
					new User[] { u3, u1, u2 }
			}
		};
	}
}
