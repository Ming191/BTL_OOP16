public class State {
    private int currentState;

    public void setCurrentState(int p_state) {
        this.currentState = p_state;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    State() {
        currentState = states.MAIN_MENU.value;
    }

    public enum states {
        MAIN_MENU(-1),
        EXIT(0),
        ADD(1),
        REMOVE(2),
        UPDATE(3),
        DISPLAY(4),
        LOOKUP(5),
        SEARCH(6),
        GAME(7),
        IMPORT_FROM_FILE(8),
        EXPORT_TO_FILE(9);

        private final int value;

        states(int p_val) {
            this.value = p_val;
        }
    }

    public void printStatesList() {
        for (states state : states.values()) {
            if(state == states.MAIN_MENU) continue;
            System.out.println("[" + state.value + "]" + " " + state.name());
        }
    }
}
