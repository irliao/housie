package com.irliao.housie.handler;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class InputHandlerTest {

    private static final String MOCK_PROMPT_MESSAGE = "MOCKED_PROMPT";
    private static final String MOCK_ERROR_MESSAGE = "MOCKED_ERROR";

    private final Scanner mockScanner = Mockito.mock(Scanner.class);
    private final InputHandler inputHandler = new InputHandler(mockScanner);

    @Test
    public void testRequestNumber() {
        when(mockScanner.nextLine()).thenReturn("10");
        int numEntered = inputHandler.requestNumber(MOCK_PROMPT_MESSAGE, MOCK_ERROR_MESSAGE, 0,
                                                    inputNum -> inputNum < 15);
        assertEquals(10, numEntered);
    }

    @Test
    public void testRequestNumber_failValidation() {
        when(mockScanner.nextLine()).thenReturn("100")
                                    .thenReturn("10");
        int numEntered = inputHandler.requestNumber(MOCK_PROMPT_MESSAGE, MOCK_ERROR_MESSAGE, 0,
                                                    inputNum -> inputNum < 15);
        assertEquals(10, numEntered);
    }

    @Test
    public void testRequestNumber_invalidInput() {
        when(mockScanner.nextLine()).thenReturn("NOT_A_NUMEBR")
                                    .thenReturn("10");
        int numEntered = inputHandler.requestNumber(MOCK_PROMPT_MESSAGE, MOCK_ERROR_MESSAGE, 0,
            inputNum -> inputNum < 15);
        assertEquals(10, numEntered);
    }
}
