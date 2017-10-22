package ovh.not.golocommandframework;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Command {
    private final Method executor;
    private final String name;
    private final String source;

    Command(Method executor, String name, String source) {
        this.executor = executor;
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    void execute(GuildMessageReceivedEvent event, List<String> args) {
        try {
            executor.invoke(null, source, event, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
