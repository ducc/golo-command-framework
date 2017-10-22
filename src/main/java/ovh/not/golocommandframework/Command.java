package ovh.not.golocommandframework;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Command {
    private final String name;
    private final Method method;

    Command(String name, Method method) {
        this.name = name;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    void execute(GuildMessageReceivedEvent event, List<String> args) {
        try {
            method.invoke(null, event, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
