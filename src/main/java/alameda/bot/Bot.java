package alameda.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class Bot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUserName;

    private Set<Ability> abilities;

    public Bot(String botToken, String botUserName){
        this.botToken = botToken;
        this.botUserName = botUserName;
        this.abilities = new HashSet<>();
    }

    public Bot(String botToken, String botUserName, DefaultBotOptions botOptions){
        super(botOptions);
        this.botToken = botToken;
        this.botUserName = botUserName;
        this.abilities = new HashSet<>();
    }

    public Bot(String botToken, String botUserName, Set<Class<? extends Ability>> abilities){
        this.botToken = botToken;
        this.botUserName = botUserName;
        abilities.forEach(this::addAbility);
    }

    public <T extends Ability> void addAbility(Class<T> ability) {
        try {
            this.abilities.add(ability.getDeclaredConstructor(TelegramLongPollingBot.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void processUpdate(Update update){
        abilities.forEach(ability -> {
            if (ability.triggered(update)){
                ability.performActions(update);
            }
        });
    }
}
