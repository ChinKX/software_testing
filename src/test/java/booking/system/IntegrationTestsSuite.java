package Booking.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value = Suite.class)
@SuiteClasses(value = {
		BookingIntegrationTests.class,
		UserIntegrationTests.class,
		WaitingListIntegrationTests.class		
})
public class IntegrationTestsSuite {

}
