package com.irliao.housie.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SettingHandlerTest {

    private final Scanner mockScanner = Mockito.mock(Scanner.class);
    private final InputHandler inputHandler = new InputHandler(mockScanner);
    private final SettingHandler settingHandler = new SettingHandler(mockScanner);

    @Before
    public void before() {
        ReflectionTestUtils.setField(settingHandler, "inputHandler", inputHandler);
    }

    @Test
    public void testRequestUserInputsForSetting() {
        when(mockScanner.nextLine()).thenReturn("1")
                                    .thenReturn("90")
                                    .thenReturn("5")
                                    .thenReturn("3")
                                    .thenReturn("10")
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(1, settingHandler.getNumberRangeStart());
        assertEquals(90, settingHandler.getNumberRangeEnd());
        assertEquals(5, settingHandler.getNumberOfPlayers());
        assertEquals(3, settingHandler.getNumberOfRows());
        assertEquals(10, settingHandler.getNumberOfCols());
        assertEquals(5, settingHandler.getNumberPerRow());
    }

    @Test
    public void testRequestUserInputsForSetting_defaultValues() {
        when(mockScanner.nextLine()).thenReturn("") // num range start
                                    .thenReturn("") // num range end
                                    .thenReturn("") // num player
                                    .thenReturn("") // num row
                                    .thenReturn("") // num col
                                    .thenReturn(""); // num per row

        settingHandler.requestUserInputsForSetting();
        assertEquals(1, settingHandler.getNumberRangeStart());
        assertEquals(90, settingHandler.getNumberRangeEnd());
        assertEquals(5, settingHandler.getNumberOfPlayers());
        assertEquals(3, settingHandler.getNumberOfRows());
        assertEquals(10, settingHandler.getNumberOfCols());
        assertEquals(5, settingHandler.getNumberPerRow());
    }

    @Test
    public void testRequestUserInputsForSetting_validateRangeStart() {
        when(mockScanner.nextLine()).thenReturn("3") // num range start
                                    .thenReturn("90") // num range end
                                    .thenReturn("5")
                                    .thenReturn("3")
                                    .thenReturn("10")
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(3, settingHandler.getNumberRangeStart());
    }

    @Test
    public void testRequestUserInputsForSetting_badRangeStart() {
        when(mockScanner.nextLine()).thenReturn("0") // bad num range start
                                    .thenReturn("1") // good num range start
                                    .thenReturn("90") // num range end
                                    .thenReturn("5")
                                    .thenReturn("3")
                                    .thenReturn("10")
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(1, settingHandler.getNumberRangeStart());
    }

    @Test
    public void testRequestUserInputsForSetting_validateRangeEnd() {
        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("15") // good num range end
                                    .thenReturn("5")
                                    .thenReturn("3")
                                    .thenReturn("10")
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(15, settingHandler.getNumberRangeEnd());
    }

    @Test
    public void testRequestUserInputsForSetting_badRangeEnd() {
        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("4") // bad num range end
                                    .thenReturn("90") // good num range end
                                    .thenReturn("5")
                                    .thenReturn("3")
                                    .thenReturn("10")
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(90, settingHandler.getNumberRangeEnd());
    }

    @Test
    public void testRequestUserInputsForSetting_badRangeEndForRangeStart() {
        when(mockScanner.nextLine()).thenReturn("6") // different range start
                                    .thenReturn("9") // bad num range end (need at least 5 numbers in range)
                                    .thenReturn("10") // good num range end
                                    .thenReturn("5")
                                    .thenReturn("1")
                                    .thenReturn("5")
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(10, settingHandler.getNumberRangeEnd());
    }

    @Test
    public void testRequestUserInputsForSetting_validatePlayers() {
        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("90") // num range end
                                    .thenReturn("1") // bad num of players
                                    .thenReturn("5") // good num of players
                                    .thenReturn("3")
                                    .thenReturn("10")
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(5, settingHandler.getNumberOfPlayers());
    }

    @Test
    public void testRequestUserInputsForSetting_validateRow() {
        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("90") // num range end
                                    .thenReturn("5") // num player
                                    .thenReturn("0") // bad num row
                                    .thenReturn("91") // bad num row
                                    .thenReturn("3") // good num row
                                    .thenReturn("10")
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(3, settingHandler.getNumberOfRows());
    }

    @Test
    public void testRequestUserInputsForSetting_validateCol() {
        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("90") // num range end
                                    .thenReturn("5") // num player
                                    .thenReturn("3") // num row
                                    .thenReturn("0") // bad num col
                                    .thenReturn("10") // good num col
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(10, settingHandler.getNumberOfCols());

        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("90") // num range end
                                    .thenReturn("5") // num player
                                    .thenReturn("1") // 1 row must then have 6 col
                                    .thenReturn("4") // bad num col
                                    .thenReturn("5") // good num col
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(5, settingHandler.getNumberOfCols());
    }

    @Test
    public void testRequestUserInputsForSetting_validateColCornerCase() {
        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("6") // num range end
                                    .thenReturn("5") // num player
                                    .thenReturn("2") // 2 row must then have 3 col
                                    .thenReturn("2") // bad num col
                                    .thenReturn("3") // num col must at least be 3 to have at least 5 numbers
                                    .thenReturn("3");

        settingHandler.requestUserInputsForSetting();
        assertEquals(3, settingHandler.getNumberOfCols());

        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("90") // num range end
                                    .thenReturn("5") // num player
                                    .thenReturn("1") // 1 row must then have 5 col
                                    .thenReturn("4") // bad num col
                                    .thenReturn("5") // num col must at least be 5 to have at least 5 numbers
                                    .thenReturn("5");

        settingHandler.requestUserInputsForSetting();
        assertEquals(5, settingHandler.getNumberOfCols());
    }

    @Test
    public void testRequestUserInputsForSetting_validateNumPerRow() {
        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("90") // num range end
                                    .thenReturn("5") // num player
                                    .thenReturn("3") // num row
                                    .thenReturn("10") // num col
                                    .thenReturn("0") // bad num per row
                                    .thenReturn("5"); // good num per row

        settingHandler.requestUserInputsForSetting();
        assertEquals(5, settingHandler.getNumberPerRow());

        when(mockScanner.nextLine()).thenReturn("1") // num range start
                                    .thenReturn("5") // num range end
                                    .thenReturn("5") // num player
                                    .thenReturn("1") // num row
                                    .thenReturn("5") // num col
                                    .thenReturn("4") // bad num per row
                                    .thenReturn("5"); // good num per row

        settingHandler.requestUserInputsForSetting();
        assertEquals(5, settingHandler.getNumberPerRow());

        when(mockScanner.nextLine()).thenReturn("6") // different range start
                                    .thenReturn("15") // good num range end
                                    .thenReturn("5") // num plauyer
                                    .thenReturn("3") // num row
                                    .thenReturn("10") // num col
                                    .thenReturn("3"); // has to be 3 per row since we only have range of 9 for 3 rows

        settingHandler.requestUserInputsForSetting();
        assertEquals(15, settingHandler.getNumberRangeEnd());
    }
}
