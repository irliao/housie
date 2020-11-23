package com.irliao.housie.handler;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Scanner;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class SettingHandlerTest {

    private final Scanner mockScanner = Mockito.mock(Scanner.class);
    private final InputHandler mockInputHandler = Mockito.mock(InputHandler.class);
    private final SettingHandler settingHandler = new SettingHandler(mockScanner);

    @Test
    public void testRequestUserInputsForSetting() {
        when(mockInputHandler.requestNumber(anyString(), anyString(), anyInt(), any(Predicate.class))).thenReturn(1);
        ReflectionTestUtils.setField(settingHandler, "inputHandler", mockInputHandler);

        settingHandler.requestUserInputsForSetting();
        assertEquals(1, settingHandler.getNumberRangeEnd());
        assertEquals(1, settingHandler.getNumberOfPlayers());
        assertEquals(1, settingHandler.getNumberOfRows());
        assertEquals(1, settingHandler.getNumberOfCols());
        assertEquals(1, settingHandler.getNumberPerRow());
    }
}
