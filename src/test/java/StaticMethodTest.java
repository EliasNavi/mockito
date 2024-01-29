import org.example.DatabaseConnector;
import org.example.LetterSender;
import org.example.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageService.class)
public class StaticMethodTest {

    @Test
    public void staticMethodMock() {
        mockStatic(MessageService.class);
        LetterSender letterSender = new LetterSender(
            new DatabaseConnector(),
            new MessageService()
        );
        letterSender.getStatic("Test");
        verify(MessageService.class, times(1));
        MessageService.getString("Test");
    }
}
