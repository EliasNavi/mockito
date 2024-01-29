import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.example.DatabaseConnector;
import org.example.LetterSender;
import org.example.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.*;

public class MockitoTest {

    // --add-opens java.base/java.lang=ALL-UNNAMED

    @Test
    public void justMockTest() {
        MessageService mockMessageService = mock(MessageService.class);
        DatabaseConnector databaseConnector = new DatabaseConnector();
        LetterSender letterSender = new LetterSender(
                databaseConnector,
                mockMessageService
        );
        Assert.assertEquals(
                databaseConnector,
                letterSender.getDatabaseConnector()
        );
    }

    @Test
    public void mockAndStubTest() {
        MessageService mockMessageService = mock(MessageService.class);
        DatabaseConnector mockDatabaseConnector = mock(DatabaseConnector.class);
        LetterSender letterSender = new LetterSender(
                mockDatabaseConnector,
                mockMessageService
        );
        Set<String> emails = new HashSet<>();
        emails.add("email");
        when(mockDatabaseConnector.getSubscribers()).thenReturn(emails);
        Assert.assertEquals(1, letterSender.numberOfSubs());
    }

    @Test(expected = Error.class)
    public void spyTest() {
        LetterSender letterSender = new LetterSender(
                new DatabaseConnector(),
                new MessageService()
        );
        LetterSender spySender = spy(letterSender);
        when(spySender.numberOfSubs()).thenReturn(0);
        spySender.sendMessage("test");
    }

    @Test
    public void mockitoVerify() {
        MessageService mockMessageService = mock(MessageService.class);
        DatabaseConnector mockDatabaseConnector = mock(DatabaseConnector.class);
        LetterSender letterSender = new LetterSender(
                mockDatabaseConnector,
                mockMessageService
        );
        letterSender.systemStatus("online");
        verify(mockMessageService).checkStatus("online");
    }

    @Test
    public void privateMethodMock() throws Exception {
        MessageService mockMessageService = mock(MessageService.class);
        DatabaseConnector mockDatabaseConnector = mock(DatabaseConnector.class);
        LetterSender letterSender = new LetterSender(
                mockDatabaseConnector,
                mockMessageService
        );
        String getName = Whitebox.invokeMethod(letterSender, "getName", "123");
        Assert.assertEquals("LetterSender", getName);
    }

    @Test
    public void argumentCaptor() {
        MessageService mockMessageService = mock(MessageService.class);
        DatabaseConnector databaseConnectorSpy = spy(DatabaseConnector.class);
        LetterSender letterSender = new LetterSender(
                databaseConnectorSpy,
                mockMessageService
        );
        ArgumentCaptor<String> capturedString = ArgumentCaptor.forClass(
                String.class
        );
        letterSender.sendMessage("test");
        verify(databaseConnectorSpy).setDatabase(capturedString.capture());
        String capturedStringValue = capturedString.getValue();
        Assert.assertEquals(capturedStringValue, "new");
    }

    @Test
    public void argumentMatchersCheckAnyArg() {
        DatabaseConnector databaseConnectorSpy = spy(DatabaseConnector.class);
        databaseConnectorSpy.checkDatabase("testDB");
        verify(databaseConnectorSpy).check(any(String.class));
    }

    @Test
    public void argumentMatchersCheckWithAnyArg() {
        DatabaseConnector databaseConnectorSpy = spy(DatabaseConnector.class);
        when(databaseConnectorSpy.checkDatabase(any(String.class)))
                .thenReturn(true);
        Assert.assertTrue(databaseConnectorSpy.checkDatabase("123"));
    }

    private Set<String> users = new HashSet<>();

    @Test
    public void mockitoAnswer() {
        MessageService mockMessageService = mock(MessageService.class);
        DatabaseConnector databaseConnectorSpy = spy(DatabaseConnector.class);
        LetterSender letterSender = new LetterSender(
                databaseConnectorSpy,
                mockMessageService
        );
        when(databaseConnectorSpy.getSubscribers()).thenAnswer(new GetUsers());
        users.add("TestUser");
        Assert.assertEquals(letterSender.numberOfSubs(), 1);

        String newUser = "NewUser";
        when(databaseConnectorSpy.addSubscriber(newUser))
                .thenAnswer(new AddUsers());
        letterSender.addUser(newUser);
        Assert.assertEquals(letterSender.numberOfSubs(), 2);
    }

    class GetUsers implements Answer<Object> {

        @Override
        public Object answer(InvocationOnMock invocationOnMock)
                throws Throwable {
            return users;
        }
    }

    class AddUsers implements Answer<Object> {

        @Override
        public Object answer(InvocationOnMock invocationOnMock)
                throws Throwable {
            String user = (String) invocationOnMock.getArguments()[0];
            users.add(user);
            return user;
        }
    }

    @Test
    public void mockingVoidBySpy() {
        DatabaseConnector databaseConnectorSpy = spy(DatabaseConnector.class);
        String line = "text";
        doNothing().when(databaseConnectorSpy).write(line);
        databaseConnectorSpy.write(line);
    }

    @Test
    public void mockingVoidByMock() {
        DatabaseConnector databaseConnectorMock = mock(DatabaseConnector.class);
        String line = "text";
        doCallRealMethod().when(databaseConnectorMock).write(line);
        databaseConnectorMock.write(line);
    }

    @Test(expected = Error.class)
    public void throwingByMock() {
        DatabaseConnector databaseConnectorMock = mock(DatabaseConnector.class);
        String line = "text";
        doThrow(Error.class).when(databaseConnectorMock).write(line);
        databaseConnectorMock.write(line);
    }
}
