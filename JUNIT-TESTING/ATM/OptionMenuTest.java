package ATM;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;

public class OptionMenuTest {
    private OptionMenu optionMenu;
    private InputStream originalSystemIn;
    private PrintStream originalSystemOut;

    @BeforeEach
    public void setUp() {
        optionMenu = new OptionMenu();
        originalSystemIn = System.in;
        originalSystemOut = System.out;
    }

    @Test
    public void testLoginWithValidCredentials() {
        // Add a sample account to the data
        optionMenu.data.put(952141, new Account(952141, 191904, 1000, 5000));

        // Simulate user input for a valid login
        String input = "952141\n191904\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            optionMenu.getLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reset System.in and System.out
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);

        // Verify the output and that the user was able to exit
        String expectedOutput = "\nEnter your customer number: \nEnter your PIN number: \n" +
                "\nSelect the account you want to access: \n Type 1 - Checkings Account\n Type 2 - Savings Account\n Type 3 - Exit\n" +
                "\nChoice: \n" +
                "\nThank You for using this ATM.\n";
        assertEquals(expectedOutput, outputStream.toString());

        // Add assertions to validate the behavior of the login process
        //assertTrue(/* Add your assertions here */);
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        // Add a sample account to the data
        optionMenu.data.put(952141, new Account(952141, 191904, 1000, 5000));

        // Simulate user input for an invalid login (wrong PIN)
        String input = "952141\n123456\n952141\n191904\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            optionMenu.getLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reset System.in and System.out
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);

        // Verify the output and that the user was able to exit after a second attempt
        String expectedOutput = "\nEnter your customer number: \nEnter your PIN number: \n" +
                "\nWrong Customer Number or Pin Number\n" +
                "\nEnter your customer number: \nEnter your PIN number: \n" +
                "\nSelect the account you want to access: \n Type 1 - Checkings Account\n Type 2 - Savings Account\n Type 3 - Exit\n" +
                "\nChoice: \n" +
                "\nThank You for using this ATM.\n";
        assertEquals(expectedOutput, outputStream.toString());

        // Add assertions to validate the behavior of the login process
        //assertTrue(/* Add your assertions here */);
    }

    // Add more tests for other methods and scenarios as needed
}
