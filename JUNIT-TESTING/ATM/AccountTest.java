package ATM;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;


public class AccountTest {
    private Account account;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;

//    @BeforeEach
//    public void setUp() {
//        // Create an instance of the Account class before each test
//        account = new Account(12345, 6789, 1000.0, 500.0);
//    }


    @Before
    public void setUpStreams() {
        // Redirect System.out to capture printed messages
       // account = new Account(12345, 6789, 1000.0, 500.0);
        System.setOut(new PrintStream(outContent));

    }



    @Before
    public void setUp() {
        account = new Account(12345, 6789, 1000.0, 500.0);
    }




    @Test
    public void testValidCheckingWithdraw() {
        account.calcCheckingWithdraw(200.0);
        assertEquals(800.0, account.getCheckingBalance());
    }


    @Test
    public void testInvalidCheckingWithdraw() {
        double initialBalance = account.getCheckingBalance();
        account.calcCheckingWithdraw(1200.0); // Attempt to withdraw more than the balance
        assertEquals(initialBalance, account.getCheckingBalance());
    }

    @Test
    public void testValidSavingWithdraw() {
        account.calcSavingWithdraw(100.0);
        assertEquals(400.0, account.getSavingBalance());
    }

    @Test
    public void testInvalidSavingWithdraw() {
        double initialBalance = account.getSavingBalance();
        account.calcSavingWithdraw(600.0); // Attempt to withdraw more than the balance
        assertEquals(initialBalance, account.getSavingBalance());
    }

    @Test
    public void testValidCheckingDeposit() {
        account.calcCheckingDeposit(300.0);
        assertEquals(1300.0, account.getCheckingBalance());
    }

    @Test
    public void testInvalidCheckingDeposit() {
        double initialBalance = account.getCheckingBalance();
        account.calcCheckingDeposit(-100.0); // Attempt to deposit a negative amount
        assertEquals(initialBalance, account.getCheckingBalance());
    }

    @Test
    public void testValidSavingDeposit() {
        account.calcSavingDeposit(150.0);
        assertEquals(650.0, account.getSavingBalance());
    }

    @Test
    public void testInvalidSavingDeposit() {
        double initialBalance = account.getSavingBalance();
        account.calcSavingDeposit(-50.0); // Attempt to deposit a negative amount
        assertEquals(initialBalance, account.getSavingBalance());
    }

    @Test
    public void testValidCheckTransfer() {
        account.calcCheckTransfer(200.0);
        assertEquals(800.0, account.getCheckingBalance());
        assertEquals(700.0, account.getSavingBalance());
    }

    @Test
    public void testInvalidCheckTransfer() {
        double initialCheckingBalance = account.getCheckingBalance();
        double initialSavingBalance = account.getSavingBalance();
        account.calcCheckTransfer(1200.0); // Attempt to transfer more than the checking balance
        assertEquals(initialCheckingBalance, account.getCheckingBalance());
        assertEquals(initialSavingBalance, account.getSavingBalance());
    }

    @Test
    public void testValidSavingTransfer() {
        account.calcSavingTransfer(100.0);
        assertEquals(1100.0, account.getCheckingBalance());
        assertEquals(400.0, account.getSavingBalance());
    }

    @Test
    public void testInvalidSavingTransfer() {
        double initialCheckingBalance = account.getCheckingBalance();
        double initialSavingBalance = account.getSavingBalance();
        account.calcSavingTransfer(600.0); // Attempt to transfer more than the saving balance
        assertEquals(initialCheckingBalance, account.getCheckingBalance());
        assertEquals(initialSavingBalance, account.getSavingBalance());
    }


    @Test
    public void testGetCheckingWithdrawInputWithValidAmount() {
        // Simulate user input with a valid amount (e.g., "200.0")
        ByteArrayInputStream in = new ByteArrayInputStream("200.0\n".getBytes());
        System.setIn(in);

        Account account = new Account();
        account.getCheckingWithdrawInput();

        // Check if the output contains the new balance
        assertTrue(outContent.toString().contains("Current Checkings Account Balance: $800.00"));
    }


    @Test
    public void testGetCheckingWithdrawInputWithInvalidAmount() {
        // Simulate user input with an amount greater than the balance (e.g., "1200.0")
        ByteArrayInputStream in = new ByteArrayInputStream("1200.0\n".getBytes());
        System.setIn(in);

        Account account = new Account();
        account.getCheckingWithdrawInput();

        // Check if the output contains the "Balance Cannot be Negative" message
        assertTrue(outContent.toString().contains("Balance Cannot be Negative."));
    }


    @Test
    public void testGetCheckingWithdrawInputWithInvalidInput() {
        // Simulate user input with an invalid input (e.g., "invalid")
        ByteArrayInputStream in = new ByteArrayInputStream("invalid\n".getBytes());
        System.setIn(in);

        Account account = new Account();
        account.getCheckingWithdrawInput();

        // Check if the output contains the "Invalid Choice" message
        assertTrue(outContent.toString().contains("Invalid Choice."));
    }


    @Test
    public void testGetSavingDepositInputWithValidAmount() {
        // Simulate user input with a valid amount (e.g., "200.0")
        ByteArrayInputStream in = new ByteArrayInputStream("200.0\n".getBytes());
        System.setIn(in);

        Account account = new Account();
        account.getSavingDepositInput();

        // Check if the output contains the new balance
        assertTrue(outContent.toString().contains("Current Savings Account Balance: $200.00"));
    }

    @Test
    public void testGetSavingDepositInputWithInvalidAmount() {
        // Simulate user input with a negative amount (e.g., "-100.0")
        ByteArrayInputStream in = new ByteArrayInputStream("-100.0\n".getBytes());
        System.setIn(in);

        Account account = new Account();
        account.getSavingDepositInput();

        // Check if the output contains the "Balance Cannot be Negative" message
        assertTrue(outContent.toString().contains("Balance Cannot Be Negative."));
    }

    @Test
    public void testGetSavingDepositInputWithInvalidInput() {
        // Simulate user input with an invalid input (e.g., "invalid")
        ByteArrayInputStream in = new ByteArrayInputStream("invalid\n".getBytes());
        System.setIn(in);

        Account account = new Account();
        account.getSavingDepositInput();

        // Check if the output contains the "Invalid Choice" message
        assertTrue(outContent.toString().contains("Invalid Choice."));
    }


    @Test
    public void testGetTransferInputWithValidTransferFromCheckingsToSavings() {
        // Simulate user input to transfer from Checkings to Savings
        ByteArrayInputStream in = new ByteArrayInputStream("1\n200.0\n".getBytes());
        System.setIn(in);

        Account account = new Account(12345, 6789, 1000.0, 500.0);
        account.getTransferInput("Checkings");

        // Check if the output contains the new balances
        assertTrue(outContent.toString().contains("Current Checkings Account Balance: $800.00"));
        assertTrue(outContent.toString().contains("Current Savings Account Balance: $700.00"));
    }

    @Test
    public void testGetTransferInputWithValidTransferFromSavingsToCheckings2() {
        // Simulate user input to transfer from Savings to Checkings
        ByteArrayInputStream in = new ByteArrayInputStream("1\n200.0\n".getBytes());
        System.setIn(in);

        Account account = new Account(12345, 6789, 1000.0, 500.0);
        account.getTransferInput("Savings");

        // Check if the balances have been updated correctly
        assertEquals(1200.0, account.getCheckingBalance());
        assertEquals(300.0, account.getSavingBalance());

        // Check if the output contains the new balances
        assertTrue(outContent.toString().contains("Current Checkings Account Balance: $1,200.00"));
        assertTrue(outContent.toString().contains("Current Savings Account Balance: $300.00"));
    }



    @Test
    public void testGetTransferInputWithValidTransferFromSavingsToCheckings() {
        // Simulate user input to transfer from Savings to Checkings
        ByteArrayInputStream in = new ByteArrayInputStream("1\n200.0\n".getBytes());
        System.setIn(in);

        Account account = new Account(12345, 6789, 1000.0, 500.0);
        account.getTransferInput("Savings");

        // Check if the output contains the new balances

        assertEquals(1000.0, account.getCheckingBalance());
        assertEquals(500.0, account.getSavingBalance());

        assertTrue(outContent.toString().contains("Current checkings account balance: $1200.00"));
        assertTrue(outContent.toString().contains("Current savings account balance: $300.00"));
    }


    @Test
    public void testGetTransferInputWithInvalidTransferAmount() {
        // Simulate user input to transfer an invalid amount
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1500.0\n".getBytes());
        System.setIn(in);

        Account account = new Account(12345, 6789, 1000.0, 500.0);
        account.getTransferInput("Checkings");

        // Check if the output contains the "Balance Cannot Be Negative" message
        assertTrue(outContent.toString().contains("Balance Cannot Be Negative."));
    }


    @Test
    public void testGetTransferInputWithInvalidChoice() {
        // Simulate user input with an invalid choice
        ByteArrayInputStream in = new ByteArrayInputStream("3\n".getBytes());
        System.setIn(in);

        Account account = new Account(12345, 6789, 1000.0, 500.0);
        account.getTransferInput("Checkings");

        // Check if the output contains the "Invalid Choice" message
        assertTrue(outContent.toString().contains("Invalid Choice."));
    }


    @Test
    public void testGetTransferInputWithExitOption() {
        // Simulate user choosing to exit
        ByteArrayInputStream in = new ByteArrayInputStream("2\n".getBytes());
        System.setIn(in);

        Account account = new Account(12345, 6789, 1000.0, 500.0);
        account.getTransferInput("Checkings");

        // Check if the output does not contain balance messages
        assertFalse(outContent.toString().contains("Current Checkings Account Balance:"));
        assertFalse(outContent.toString().contains("Current Savings Account Balance:"));
    }


















    @After
    public void restoreStreams() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
        //System.setErr(originalErr);
    }




}