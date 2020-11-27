# Housie

## How to Install
```bash
mvn clean install
```

## How to Run
Terminal:
```bash
./run.sh
```
IntelliJ:

Run src/main/java/com/irliao/housie/Application.java

## Assumptions
- Console output (ex. prompt, error message, etc) does not have to be exact and can be slightly changed. 
  Ticket size can be entered as 2 separate user input (row and col entered in 2 separate inputs).
- User can press "Enter" for default settings. User can also press "Enter" for next number.
- User must enter range 5-n instead of 1-n since there is an "Early Five" combination
- Key presses will be accompanied by "Enter" since Java console requires "Return" key for Scanner. 
  Due to Java's console limitation, JLine or other library required to provide true key press listener. 
- Empty slots are intentional part of design.
  Otherwise, the ticket size only needs to be numberOfRows x numbersPerRow
- Each row must have at least 1 number.
- First person to win can be determined by the player parsing order.

## Future Improvements
- Integrate logger
- Improve unit tests

## Author
Ryan Liao
