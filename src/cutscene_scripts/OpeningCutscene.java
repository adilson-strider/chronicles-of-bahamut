package cutscene_scripts;

import cutscene.CutsceneAction;
import cutscene.CutsceneManager;
import cutscene.actions.*;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import ui.UI;

import java.awt.*;

import static main.GameManager.cutsceneManager;

public class OpeningCutscene implements IState {

    CutsceneAction currentEvent;
    String cutsceneID = "opening";

    public OpeningCutscene() {
        // Adicione eventos à cutscene
        GameManager.cutsceneManager.startCutscene();
        GameManager.cutsceneManager.addEvent(new OpeningAction());

        GameManager.cutsceneManager.addEvent(new FadeOutAction(2, true));

        GameManager.cutsceneManager.addEvent(new EndCutsceneAction());
    }

    @Override
    public void update() {
        if (GameManager.cutsceneManager.isCutsceneActive()) {
            GameManager.cutsceneManager.update();
        }

        // Se o evento atual é um diálogo, atualize o diálogo exibido
        currentEvent = cutsceneManager.getCurrentEvent();
        if (currentEvent instanceof DialogueAction) {
            UI.getInstance().currentDialogue = ((DialogueAction) currentEvent).getCurrentDialogue();
        }

        if(currentEvent.isFinished()){
            StateMachine.getInstance().change("InGame");
            cutsceneManager.markCutsceneAsWatched(cutsceneID);
            cutsceneManager.eraseList();
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        if (currentEvent != null) {
            currentEvent.draw(g2);
        }
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {
        // Limpar o estado da cutscene quando sair do estado

    }

    @Override
    public String getName() {
        return "Opening";
    }
}
