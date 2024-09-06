package cutscene_scripts;

import cutscene.CutsceneAction;
import cutscene.CutsceneManager;
import cutscene.actions.DialogueAction;
import cutscene.actions.ItemGivenAction;
import cutscene.actions.MoveCharacterAction;
import cutscene.actions.StopAnimationAction;
import entity.npcs.NPC;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import object.swords.BeginnerBlade;
import quest.BlacksmithQuestObjective;
import quest.Quest;
import ui.UI;

import java.awt.*;
import java.util.Objects;

import static main.GameManager.currentMap;
import static main.GameManager.npcs;

public class BlacksmithCutscene implements IState {

    private final CutsceneManager cutsceneManager = new CutsceneManager();
    CutsceneAction currentEvent;

    NPC npc;

    public BlacksmithCutscene() {

        // Adicione eventos à cutscene
        cutsceneManager.startCutscene();

        for (NPC npc : npcs.get(3)) {
            if (npc != null) {
                if(Objects.equals(npc.name, "Blacksmith")){
                   this.npc = npc;
                }
            }
        }

        cutsceneManager.addEvent(new MoveCharacterAction(npc,
                new Point(GameManager.tileSize * 9, GameManager.tileSize * 11), 2));

        cutsceneManager.addEvent(new StopAnimationAction(npc));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Ah, vejo que temos um visitante...",
                "Não é todo dia que um estranho entra na minha forja.",
                "O que te traz aqui, jovem?"
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Olá, mestre ferreiro.",
                "Sou novo na cidade e estou em busca de trabalho.",
                "Dizem que suas armas são as melhores do reino.",
                "Eu gostaria de aprender com você...",
                "...ou pelo menos, ajudar no que puder."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Hm, um aprendiz, hein? Não é fácil, garoto.",
                "O trabalho aqui é duro e o fogo não perdoa os fracos.",
                "Mas, vamos ver o que você é capaz.",
                "Primeiro, preciso de um favor."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Qualquer coisa! Só diga o que precisa."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Minha última encomenda de minério está atrasada.",
                "O mensageiro deveria ter chegado ontem com os lingotes de ferro.",
                "O caminho para a mina de ferro é traiçoeiro, e temo que algo possa ter acontecido.",
                "Se você puder investigar e trazer o minério, será um bom começo.",
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Pode deixar comigo. Vou encontrar o mensageiro e trazer o minério de volta."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Bom garoto. Leve isso com você.",
                "É só uma adaga simples, mas será útil se encontrar problemas pelo caminho. ",
                "E lembre-se, a mina fica ao sudoeste daqui, entre os bosques sombrios. Boa sorte."
        }));

        // criar evento GivenItemAction
        cutsceneManager.addEvent(new ItemGivenAction(new BeginnerBlade()));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Obrigado, mestre ferreiro. Não vou desapontá-lo."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Espero que não. Agora, vá. E cuide-se."
        }));

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
        npc.speed = 1;
    }

    @Override
    public void onExit() {
        // A quest do Ferreiro é a primeira
        Quest blackSmith = GameManager.questManager.getQuests().getFirst();

        // Marca o primeiro objetivo como completo
        blackSmith.getObjectives().get(BlacksmithQuestObjective.TALK_TO_BLACKSMITH.getIndex()).setCompleted(true);
    }

    @Override
    public String getName() {
        return "BlackSmith";
    }
}
