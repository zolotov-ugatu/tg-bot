package alameda.bot;

import alameda.utils.Tools;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

public class WorkTimerAbility extends Ability {

    private Update update;
    private String message;
    private Long chatId;

    private final String CMD_HELP = "HELP";
    private final String CMD_SET = "SET";
    private final String CMD_START = "START";
    private final String CMD_STOP = "STOP";
    private final String VAR_WORKING = "WORKING";
    private final String VAR_TALKING = "TALKING";
    private final String VAR_BREAK = "BREAK";

    private final Set<String> COMMANDS = new HashSet<>(Arrays.asList(new String[]{CMD_HELP, CMD_SET, CMD_START, CMD_STOP}));
    private final String[] VARS = {VAR_BREAK, VAR_TALKING, VAR_WORKING};

    private Map<String, Integer> vars = new HashMap<String, Integer>();

    private class Scheduler implements Runnable {
        private final long workingTime;
        private final long talkingTime;
        private final long breakTime;
        private final long chatId;
        Scheduler(long wT, long tT, long bT, long cI){
            this.workingTime = wT;
            this.talkingTime = tT;
            this.breakTime = bT;
            this.chatId = cI;
        }
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                say(chatId, "Time to work!");
                try {
                    Thread.sleep(60000 * workingTime);
                } catch (InterruptedException e) {
                    say(chatId, "Task timer stopped.");
                }
                say(chatId, "Time to talk!");
                try {
                    Thread.sleep(60000 * talkingTime);
                } catch (InterruptedException e) {
                    say(chatId, "Task timer stopped.");
                }
                say(chatId, "Time to break!");
                try {
                    Thread.sleep(60000 * breakTime);
                } catch (InterruptedException e) {
                    say(chatId, "Task timer stopped.");
                }
            }
        }
    }

    private Thread taskThread;

    public WorkTimerAbility(TelegramLongPollingBot bot) {
        super(bot);
        vars.put(VAR_WORKING, 10);
        vars.put(VAR_TALKING, 5);
        vars.put(VAR_BREAK, 3);
    }



    @Override
    public boolean triggered(Update update) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                Tools.isCommand(update.getMessage().getText(), COMMANDS);
    }

    @Override
    public void performActions(Update update) {
        this.update = update;
        this.message = update.getMessage().getText();
        this.chatId = update.getMessage().getChatId();
        this.processCommand(Tools.getCommand(message));
    }

    private void processCommand(String cmd){
        switch (cmd.toUpperCase()){
            case CMD_HELP: displayHelp(); break;
            case CMD_SET: processSetCommand(); break;
            case CMD_START: processStartCommand(); break;
            case CMD_STOP: processStopCommand(); break;
        }
    }

    private void processStopCommand() {
        if (Tools.getArgumentsArray(message) == null || Tools.getArgumentsArray(message).length > 0){
            return;
        }
        if (taskThread == null || taskThread.isInterrupted()){
            say(chatId, "Nothing to stop.");
            return;
        }
        this.taskThread.stop();
        say(chatId, "Timer stopped.");
        this.taskThread = null;
    }

    private void processStartCommand() {
        if (Tools.getArgumentsArray(message) == null || Tools.getArgumentsArray(message).length > 0){
            return;
        }
        if (this.taskThread != null && !this.taskThread.isInterrupted()){
            say(chatId, "Already started.");
            return;
        }
        this.taskThread = new Thread(new Scheduler(vars.get(VAR_WORKING), vars.get(VAR_TALKING), vars.get(VAR_BREAK), chatId));
        this.taskThread.start();
    }

    private void processSetCommand() {
        String[] argList = Tools.getArgumentsArray(message);
        if (argList == null || argList.length == 0 || argList.length > 2 ||
                Arrays.stream(this.VARS).noneMatch(var -> var.equalsIgnoreCase(argList[0]))){
            say(chatId, "Syntax:\nSET [ WORKING | TALKING | BREAK ] <VALUE>");
            return;
        }
        if (argList.length == 1 &&
                Arrays.stream(this.VARS).anyMatch(var -> var.equalsIgnoreCase(argList[0]))){
            say(chatId, argList[0].toUpperCase() + " = " + vars.get(argList[0].toUpperCase()));
            return;
        }
        if (!Tools.isInteger(argList[1]) || Integer.parseInt(argList[1]) < 1 || Integer.parseInt(argList[1]) > 60){
            say(chatId, "<VALUE> must be in interval [1...60]");
            return;
        }
        vars.put(argList[0].toUpperCase(), Integer.parseInt(argList[1]));
        say(chatId, argList[0].toUpperCase() + " set to " + argList[1]);
    }

    private void displayHelp(){
        say(chatId,
                "Commands: \n" +
                        "SET [ WORKING | TALKING | BREAK ] <VALUE>\n" +
                        "START\n" +
                        "STOP");
    }
}
