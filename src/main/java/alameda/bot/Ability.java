package alameda.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class Ability {

    private final TelegramLongPollingBot bot;

    public Ability(TelegramLongPollingBot bot){
        this.bot = bot;
    }

    protected void say(Long chatId, String msg){
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public abstract boolean triggered(Update update);

    public abstract void performActions(Update update);

}
