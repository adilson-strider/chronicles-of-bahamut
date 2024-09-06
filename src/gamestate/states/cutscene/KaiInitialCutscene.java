package gamestate.states.cutscene;

import cutscene.CutsceneAction;
import cutscene.CutsceneManager;
import cutscene.actions.*;
import entity.npcs.NPC_Blacksmith;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import object.OBJ_TurquoiseAmulet;
import object.swords.BeginnerBlade;
import tiledmap.TiledMap;
import ui.UI;

import java.awt.*;

import static main.GameManager.currentTiledMap;
import static main.GameManager.cutsceneManager;

public class KaiInitialCutscene implements IState {

    CutsceneAction currentEvent;

    String cutsceneID = "kaiInitialScene";

    public KaiInitialCutscene() {

        // GameManager.setMapAndPosition("village1", 3, 4);

        // Adicione eventos à cutscene
        cutsceneManager.startCutscene();
        cutsceneManager.addEvent(new FadeInAction());

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "O dia está começando, meu querido filho.",
                "Acorde e venha comer algo antes de iniciar suas tarefas.",
        }));

        /*cutsceneManager.addEvent(new MoveCharacterAction(GameManager.getPlayer(),
                new Point(GameManager.tileSize * 3, GameManager.tileSize * 6), 2));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Bom dia, mãe. O que há de novo hoje?",
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Estou com um pressentimento, meu filho.",
                "Há algo no ar, uma sensação de que as coisas estão mudando. ",
                "Mas não se preocupe, cuide do que é importante e mantenha seu coração firme.",
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Mudando? O que você quer dizer com isso?",
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Há velhas histórias que falam de tempos de grande escuridão e de heróis que surgem para restaurar a luz.",
                "As estrelas têm seus próprios segredos, e algo me diz que este é um momento em que esses segredos podem começar a se revelar",
        }));

        cutsceneManager.addEvent(new MoveCharacterAction(GameManager.getPlayer(),
                new Point(GameManager.tileSize * 7, GameManager.tileSize * 6), 2));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Você sempre fala em enigmas, mãe. Se algo está prestes a acontecer, qual o meu papel nisso tudo?"
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Pegue isto."
        }));

        // criar evento GivenItemAction
        cutsceneManager.addEvent(new ItemGivenAction(new OBJ_TurquoiseAmulet()));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Esta amuleto foi passado de geração em geração. ",
                "É mais do que um simples objeto; é um símbolo de esperança e coragem. ",
                "Carregue-o com você e lembre-se de que há mais do que os olhos podem ver."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Leve consigo também esta espada..."
        }));

        cutsceneManager.addEvent(new ItemGivenAction(new BeginnerBlade()));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Você precisará se defender dos perigos."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "...",
                "Eu farei isso. Então, o que eu devo fazer agora?"
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Vá explorar o mundo ao seu redor, mantenha seus sentidos alerta e seu coração aberto. ",
                "Há sinais e pistas que podem estar escondidos nas pequenas coisas. ",
                "Seu destino pode estar mais próximo do que imagina."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Entendi. Vou estar atento. Obrigado, mãe."
        }));

        cutsceneManager.addEvent(new DialogueAction(new String[]{
                "Lembre-se, meu filho, o caminho pode ser incerto, ",
                "mas a coragem e a sabedoria sempre iluminarão sua jornada. ",
                "Vá agora, e que as estrelas te guiem!"
        }));*/
    }

    @Override
    public void update() {

        if (cutsceneManager.isCutsceneActive()) {
            cutsceneManager.update();
        }
        else{
            StateMachine.getInstance().change("InGame");
            cutsceneManager.markCutsceneAsWatched(cutsceneID);
        }

        // Se o evento atual é um diálogo, atualize o diálogo exibido
        currentEvent = cutsceneManager.getCurrentEvent();
        if (currentEvent instanceof DialogueAction) {
            UI.getInstance().currentDialogue = ((DialogueAction) currentEvent).getCurrentDialogue();
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        if (currentEvent != null) {
            StateMachine.getInstance().getmStates().get("InGame").draw(g2);
            currentEvent.draw(g2);
        }
    }

    @Override
    public void onEnter() {
        // Inicializar a cutscene quando entrar no estado
    }

    @Override
    public void onExit() {
        // Limpar o estado da cutscene quando sair do estado


    }

    @Override
    public String getName() {
        return "KaiScene";
    }
}
