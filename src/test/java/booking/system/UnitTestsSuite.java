package booking.system;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value = Suite.class)
@SuiteClasses(value = {
		BookingUnitTests.class,
		UserUnitTests.class,
		WaitingListUnitTests.class		
})

public class UnitTestsSuite {

}