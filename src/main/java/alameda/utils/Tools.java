package alameda.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Set;

public final class Tools {



    public static boolean isCommand(String msg, String cmd){
        msg = msg.toLowerCase();
        cmd = cmd.toLowerCase();
        if (msg.equals(cmd)){
            return true;
        }
        return msg.split("\\s")[0].equals(cmd);
    }

    public static boolean isCommand(String msg, Set<String> cmds){
        return cmds.stream().anyMatch(cmd -> isCommand(msg, cmd));
    }

    public static String getCommand(String msg){
        return msg.split("\\s")[0];
    }

    public static String[] getArgumentsArray(String message) {
        String[] words = message.split("\\s");
        return Arrays.copyOfRange(words, 1, words.length);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
