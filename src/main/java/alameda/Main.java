package alameda;

import alameda.bot.Bot;
import alameda.bot.PingAbility;
import alameda.bot.WorkTimerAbility;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class Main {
    private static String PROXY_HOST = "195.201.140.118" /* proxy host */;
    private static Integer PROXY_PORT = 2016 /* proxy port */;
    private static String PROXY_USER = "prxusr" /* proxy user */;
    private static String PROXY_PASSWORD = "pass12" /* proxy password */;
    public static void main(String[] args) {
        parseArgs(args);


        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();


        // Set up Http proxy
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        botOptions.setProxyHost(PROXY_HOST);
        botOptions.setProxyPort(PROXY_PORT);
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
//https://t.me/socks?server=195.201.140.118&port=2016&user=prxusr&pass=pass12
        try {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
                }
            });
            Bot bot = new Bot("666311374:AAF7wQhTNXKJiPku4BujDXvotojgVK8YhRc","alameda_ufa_bot", botOptions);
            bot.addAbility(PingAbility.class);
            bot.addAbility(WorkTimerAbility.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args) {

    }
}
