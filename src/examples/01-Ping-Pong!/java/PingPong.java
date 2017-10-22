import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import ovh.not.golocommandframework.CommandFramework;

import javax.security.auth.login.LoginException;
import java.io.File;

public class PingPong {
    public static void main(String[] args) throws LoginException, InterruptedException, RateLimitedException {
        String token = System.getenv("DISCORD_TOKEN");

        CommandFramework framework = new CommandFramework.Builder()
                .setPrefix("!")
                .setCommandsDirectory(new File("src/examples/01-Ping-Pong!/golo/"))
                .build();

        JDA jda = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .addEventListener(framework.getListener())
                .buildBlocking();
    }
}
