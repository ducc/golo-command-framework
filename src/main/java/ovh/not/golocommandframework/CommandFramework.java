package ovh.not.golocommandframework;

import net.dv8tion.jda.core.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandFramework {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandFramework.class);

    private final Map<String, Command> commands = new HashMap<>();

    private String prefix;

    private Listener listener;

    private CommandFramework(String prefix, File commandsDirectory) {
        this.prefix = prefix;

        CommandLoader loader = new CommandLoader();
        try {
            List<Command> commands = loader.load(commandsDirectory);

            for (Command command : commands) {
                String name = command.getName().toLowerCase();
                System.out.println("Loaded command \"" + name + "\"");
                this.commands.put(name, command);
            }
        } catch (NoSuchMethodException | IOException e) {
            e.printStackTrace();
            return;
        }

        this.listener = new Listener(this);
    }

    public String getPrefix() {
        return prefix;
    }

    public EventListener getListener() {
        return listener;
    }

    public boolean isCommand(String identifier) {
        return commands.containsKey(identifier.toLowerCase());
    }

    public Optional<Command> getCommand(String identifier) {
        Command command = commands.get(identifier.toLowerCase());

        if (command == null) {
            return Optional.empty();
        } else {
            return Optional.of(command);
        }
    }

    public static class Builder {
        private String prefix = null;
        private File commandsDirectory = null;

        public Builder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder setCommandsDirectory(File commandsDirectory) {
            this.commandsDirectory = commandsDirectory;
            return this;
        }

        public CommandFramework build() {
            if (prefix == null) {
                throw new IllegalStateException("prefix must be set");
            }

            if (commandsDirectory == null) {
                throw new IllegalStateException("commands directory must be set");
            }

            if (!commandsDirectory.isDirectory()) {
                throw new IllegalStateException("commands directory must be a directory");
            }

            return new CommandFramework(prefix, commandsDirectory);
        }
    }
}
