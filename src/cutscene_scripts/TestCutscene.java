package cutscene_scripts;

import cutscene.*;
import cutscene.actions.*;
import cutscene.actions.OpeningAction;
import gamestate.IState;
import gamestate.StateMachine;
import ui.UI;

import java.awt.*;

public class TestCutscene implements IState {

    private CutsceneManager cutsceneManager = new CutsceneManager();
    CutsceneAction currentEvent;

    public TestCutscene() {
        // Adicione eventos à cutscene
        cutsceneManager.startCutscene();
        cutsceneManager.addEvent(new OpeningAction());
    }

    @Override
    public void update() {
        if (cutsceneManager.isCutsceneActive()) {
            cutsceneManager.update();

        } else {
            StateMachine.getInstance().change("InGame");
        }

        // Se o evento atual é um diálogo, atualize o diálogo exibido
        currentEvent = cutsceneManager.getCurrentEvent();
        if (currentEvent instanceof DialogueAction) {
            UI.getInstance().currentDialogue = ((DialogueAction) currentEvent).getCurrentDialogue();
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        StateMachine.getInstance().getmStates().get("InGame").draw(g2);

        if (currentEvent != null) {
            currentEvent.draw(g2);
        }
    }

    @Override
    public void onEnter() {
        // Inicializar a cutscene quando entrar no estado
        cutsceneManager.startCutscene();
    }

    @Override
    public void onExit() {
        // Limpar o estado da cutscene quando sair do estado

    }

    @Override
    public String getName() {
        return "testCutscene";
    }
}
