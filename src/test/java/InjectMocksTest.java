import org.example.DatabaseConnector;
import org.example.LetterSender;
import org.example.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InjectMocksTest {
    @Spy
    MessageService messageService;

    @Spy
    DatabaseConnector databaseConnector;

    @InjectMocks
    LetterSender letterSender;

    @Test
    public void injectionTest() {
        Assert.assertEquals(letterSender.numberOfSubs(), 2);
    }
}
