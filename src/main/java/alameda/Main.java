package alameda;

import alameda.bot.Bot;
import alameda.bot.PingAbility;
import alameda.bot.WorkTimerAbility;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            Bot bot = new Bot("666311374:AAF7wQhTNXKJiPku4BujDXvotojgVK8YhRc","alameda_ufa_bot");
            bot.addAbility(PingAbility.class);
            bot.addAbility(WorkTimerAbility.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
