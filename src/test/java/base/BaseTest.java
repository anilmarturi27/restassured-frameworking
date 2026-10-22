
package base;

import org.testng.annotations.BeforeMethod;
import services.BookingService;

public class BaseTest {

    protected BookingService bookingService;

    @BeforeMethod
    public void setUp() {
        bookingService = new BookingService();
        
    }
}
