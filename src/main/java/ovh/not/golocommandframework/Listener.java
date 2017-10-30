package ovh.not.golocommandframework;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Listener extends ListenerAdapter {
    private final CommandFramework framework;

    Listener(CommandFramework framework) {
        this.framework = framework;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();

        if (author == jda.getSelfUser()) {
            return;
        }

        Message message = event.getMessage();
        String content = message.getContent();
        String prefix = framework.getPrefix();

        if (content.length() <= prefix.length()) {
            return;
        }

        if (!content.substring(0, prefix.length()).equals(prefix)) {
            return;
        }

        String[] split = content.substring(prefix.length()).split("\\s+");

        if (split.length == 0) {
            return;
        }

        List<String> args = Arrays.stream(split).skip(1).collect(Collectors.toList());

        if (split[0].equals("reload")) {
            framework.reloadCommands();
            event.getChannel().sendMessage("Reloaded!").queue();
            return;
        }

        framework.getCommand(split[0]).ifPresent(command -> command.execute(event, args));
    }
}
