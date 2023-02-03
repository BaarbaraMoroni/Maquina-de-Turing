import java.util.*;

public class TuringMachine {
    private int currentState;
    private Map<Integer, Map<Character, List<Integer>>> transitionFunction;
    private Map<Integer, Map<Character, Character>> writeFunction;
    private Map<Integer, Map<Character, Integer>> moveFunction;
    private int[] acceptStates;
    private int[] rejectStates;
    private int tapePointer;
    private char[] tape;

    public TuringMachine(int currentState, Map<Integer, Map<Character, List<Integer>>> transitionFunction, Map<Integer, Map<Character, Character>> writeFunction, Map<Integer, Map<Character, Integer>> moveFunction, int[] acceptStates, int[] rejectStates, char[] tape) {
        this.currentState = currentState;
        this.transitionFunction = transitionFunction;
        this.writeFunction = writeFunction;
        this.moveFunction = moveFunction;
        this.acceptStates = acceptStates;
        this.rejectStates = rejectStates;
        this.tape = tape;
        this.tapePointer = 0;
    }

    public boolean run() {
        while (true) {
            // Check if the current state is an accept or reject state
            for (int acceptState : acceptStates) {
                if (currentState == acceptState) {
                    return true;
                }
            }
            for (int rejectState : rejectStates) {
                if (currentState == rejectState) {
                    return false;
                }
            }

            // Get the next state based on the current state and the current symbol on the tape
            List<Integer> nextStates = transitionFunction.get(currentState).get(tape[tapePointer]);
            if (nextStates == null || nextStates.size() == 0) {
                return false;
            }
            int nextState = nextStates.get(0);

            // Write the new symbol on the tape
            tape[tapePointer] = writeFunction.get(currentState).get(tape[tapePointer]);

            // Move the tape pointer
            int moveDirection = moveFunction.get(currentState).get(tape[tapePointer]);
            if (moveDirection == -1) {
                if (tapePointer == 0) {
                    char[] newTape = new char[tape.length + 1];
                    System.arraycopy(tape, 0, newTape, 1, tape.length);
                    newTape[0] = '_';
                    tape = newTape;
                } else {
                    tapePointer--;
                }
            } else {
                if (tapePointer == tape.length - 1) {
                    char[] newTape = new char[tape.length + 1];
                    System.arraycopy(tape, 0, newTape, 0, tape.length);
                    newTape[tape.length] = '_';
                    tape = newTape;
                }
                tapePointer++;
            }

            // Set the next state
            currentState = nextState;
        }
    }
}