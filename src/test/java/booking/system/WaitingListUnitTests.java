package Booking.test;

import Booking.system.*;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class WaitingListUnitTests {
	WaitingList wl;
	
	User u1 = new User("abc", "vip", false);
	User u2 = new User("Tester 1", "nonMember", false);
	User u3 = new User("cat", "member", true);
	User u4 = new User("def", "vip", false);
	User u5 = new User("Tester 2", "nonMember", false);
	User u6 = new User("dog", "member", true);
	User u7 = new User("gh", "vip", false);
	User u8 = new User("Tester 3", "nonMember", false);
	User u9 = new User("sheep", "member", true);
	
	@Before
	public void beforeClass() {
		wl = new WaitingList();
	}
	
	//to test if the system return the correct vip waiting list
	//w1
	@Test
	@Parameters(method = "paramsForGetSetVipTest")
	public void getSetVipTest(User[] initialList, User[] expectedResult) {
		wl.setAllList(initialList);
		User[] result = new User[wl.getVip().size()];
		result = wl.getVip().toArray(result);
		assertArrayEquals(expectedResult, result);
	}
	
	private Object[] paramsForGetSetVipTest() {
		return new Object[] {
			new Object[] {
				new User[] {
					u1, u2, u3
				},
				new User[] {
					u1
				}
			}
		};
	}
	
	//to test if the system return the correct member waiting list
	//w2
	@Test
	@Parameters(method = "paramsForGetSetMemberTest")
	public void getSetMemberTest(User[] initialList, User[] expectedResult) {
		wl.setAllList(initialList);
		User[] result = new User[wl.getMember().size()];
		result = wl.getMember().toArray(result);
		assertArrayEquals(expectedResult, result);
	}
	
	private Object[] paramsForGetSetMemberTest() {
		return new Object[] {
			new Object[] {
				new User[] {
					u1, u2, u3
				},
				new User[] {
					u3
				}
			}
		};
	}
	
	//to test if the system return the correct non member waiting list
	//w3
	@Test
	@Parameters(method = "paramsForGetSetNonMemberTest")
	public void getSetNonMemberTest(User[] initialList, User[] expectedResult) {
		wl.setAllList(initialList);
		User[] result = new User[wl.getNonMember().size()];
		result = wl.getNonMember().toArray(result);
		assertArrayEquals(expectedResult, result);
	}
	
	private Object[] paramsForGetSetNonMemberTest() {
		return new Object[] {
			new Object[] {
				new User[] {
					u1, u2, u3
				},
				new User[] {
					u2
				}
			}
		};
	}
	
	//to test if the system has added the user to its waiting list
	//w4
	@Test
	@Parameters(method = "paramsForAddWaitingTest")
	public void addWaitingTest(User[] initialList, User currentUser, User[] expectedResult) {
		wl.setAllList(initialList);
		
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
					new User[] { u1, u2, u3,  u4,  u5,  u6, u7, u8, u9 },
					u1,
					new User[] { u1, u4, u7, u1 }
			},
			new Object[] {
					new User[] { u2, u5, u8, u3 },
					u2,
					new User[] { u2, u5, u8, u2 }
			},
			new Object[] {
					new User[] { u3, u1, u9, u3 },
					u2,
					new User[] { u2 }
			},
			new Object[] {
					new User[] { u1, u3, u1, u5, u9, u5 },
					u2,
					new User[] { u5, u5, u2 }
			}
		};
	}

	// to test whether user's record is stored in the particular waiting list
	//w5
	@Test
	@Parameters(method = "paramsForGetWaitingTest")
	public void getWaitingTest(User[] initialList, User currentUser, boolean expectedResult) {
		wl.setAllList(initialList);
		
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
	
	// to test if the system has successfully removed the user's record from the waiting list
	//w6
	@Test
	@Parameters(method = "paramsForRemoveWaitingTest")
	public void removeWaitingTest(User[] initialList, User currentUser, User[] expectedResult) {
		wl.setAllList(initialList);
		
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
					new User[] { u1, u2, u3,  u4,  u5,  u6, u7, u8, u9 },
					u1,
					new User[] { u4, u7 }
			},
			new Object[] {
					new User[] { u1, u2, u3,  u4,  u5,  u6, u7, u8, u9 },
					u2,
					new User[] { u5, u8 }
			},
			new Object[] {
					new User[] { u1, u2, u3,  u4,  u5,  u6, u7, u8, u9 },
					u3,
					new User[] { u6, u9 }
			},
			new Object[] {
					new User[] { u1, u2, u4, u3, u1 },
					u1,
					new User[] { u4, u1 }
			},
			new Object[] {
					new User[] { u1, u2, u3,  u4,  u5,  u6, u7, u8, u9 },
					u3,
					new User[] { u6, u9 }
			},
			new Object[] {
					new User[] { u1, u4, u4, u1, u1 },
					u1,
					new User[] { u4, u4, u1, u1 }
			}
		};
	}
}