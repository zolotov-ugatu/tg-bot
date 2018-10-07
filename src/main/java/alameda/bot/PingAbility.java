package alameda.bot;

import alameda.utils.Tools;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PingAbility extends Ability {

    public PingAbility(TelegramLongPollingBot bot) {
        super(bot);
    }

    @Override
    public boolean triggered(Update update) {
        return (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().contains("ping"));
    }

    @Override
    public void performActions(Update update) {
        say(update.getMessage().getChatId(), "pong");
    }
}
