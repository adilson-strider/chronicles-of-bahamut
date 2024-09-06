package gamestate;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StateMachine {

    Map<String, IState> mStates = new HashMap<>();
    IState mCurrentState = new EmptyState();
    public static StateMachine Instance = new StateMachine();

    private StateMachine(){

    }

    public static StateMachine getInstance() {
        return Instance;
    }

    public void update() {
        mCurrentState.update();
    }

    public void draw(Graphics2D g2) {
        mCurrentState.draw(g2);
    }

    public void change(String stateName) {
        mCurrentState.onExit();
        mCurrentState = mStates.get(stateName);
        mCurrentState.onEnter();
    }

    public void add(String name, IState state) {
        mStates.put(name, state);
    }

    public void remove(String name){
        mStates.remove(name);
    }

    public IState getCurrentState(){
        return mCurrentState;
    }

    public Map<String, IState> getmStates() {
        return mStates;
    }
}
