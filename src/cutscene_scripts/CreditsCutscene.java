package cutscene_scripts;

import cutscene.CutsceneAction;
import cutscene.CutsceneManager;
import cutscene.actions.CreditsAction;
import cutscene.actions.DialogueAction;
import gamestate.IState;
import gamestate.StateMachine;
import ui.UI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CreditsCutscene implements IState {

    CutsceneManager cutsceneManager;
    CutsceneAction currentEvent;

    public CreditsCutscene(CutsceneManager cutsceneManager) {
        this.cutsceneManager = cutsceneManager;
        // Adicione eventos à cutscene
        cutsceneManager.startCutscene();

        ArrayList<String> credits = new ArrayList<>(Arrays.asList(
                "Nome 1",
                "Nome 2",
                "Nome 3"
        ));

        cutsceneManager.addEvent(new CreditsAction(credits));
    }

    @Override
    public void update() {
        if (cutsceneManager.isCutsceneActive()) {
            cutsceneManager.update();

        } else {
            StateMachine.getInstance().change("Title");
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
