package gamestate.states.game;

import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class DialogueState implements IState {

    @Override
    public void update() {

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE)){
            StateMachine.getInstance().change("InGame");
            UI.getInstance().entity.dialogueIndex = 0;
            UI.getInstance().charIndex = 0;
            UI.getInstance().combinedText = "";
        }

        if(Objects.equals(UI.getInstance().entity.npcType, "Blacksmith")){

            if(UI.getInstance().entity.dialogues[UI.getInstance().entity.dialogueSet][UI.getInstance().entity.dialogueIndex] != null){

                if(ShopState.exitMenu){
                    setTypeWriteEffect(1);
                    ShopState.exitMenu = false;
                }
                else if(ShopState.isFewMoney){
                    setTypeWriteEffect(2);
                    ShopState.isFewMoney = false;
                }
                else if(ShopState.isFullInventory){
                    setTypeWriteEffect(3);
                    ShopState.isFullInventory = false;
                }
                else if(ShopState.isEmptyInventory){
                    setTypeWriteEffect(4);
                    ShopState.isEmptyInventory = false;
                }
            }
            else{
                UI.getInstance().entity.dialogueIndex = 0;
            }
        }
        else{
            if(UI.getInstance().entity.dialogues[UI.getInstance().entity.dialogueSet][UI.getInstance().entity.dialogueIndex] != null){
                UI.getInstance().currentDialogue = UI.getInstance().entity.dialogues[UI.getInstance().entity.dialogueSet][UI.getInstance().entity.dialogueIndex];

                if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)){

                    UI.getInstance().charIndex = 0;
                    UI.getInstance().combinedText = "";
                    UI.getInstance().entity.dialogueIndex++;
                }
            }
            else{
                UI.getInstance().entity.dialogueIndex = 0;
                StateMachine.getInstance().change("InGame");
            }
        }
    }

    public void setTypeWriteEffect(int index){
        UI.getInstance().entity.dialogueIndex = index;
        UI.getInstance().currentDialogue = UI.getInstance().entity.dialogues[0][UI.getInstance().entity.dialogueIndex];
        UI.getInstance().charIndex = 0;
        UI.getInstance().combinedText = "";
    }

    @Override
    public void draw(Graphics2D g2) {


        GameManager.draw(g2);
        UI.getInstance().drawDialogueScreen(g2);

    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

        GameManager.isDialogue = true;
        ShopState.exitMenu = false;
        ShopState.isFewMoney = false;

    }

    @Override
    public String getName() {
        return "Dialogue";
    }
}
