package ovh.not.golocommandframework;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.eclipse.golo.compiler.GoloClassLoader;
import org.eclipse.golo.compiler.GoloCompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class CommandLoader {
    private static final GoloClassLoader CLASS_LOADER = new GoloClassLoader();

    List<Command> load(File dir) throws FileNotFoundException, NoSuchMethodException {
        List<Command> commands = new ArrayList<>();

        if (dir.isFile()) {
            return commands;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return commands;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                List<Command> subDirCommands = load(file);
                commands.addAll(subDirCommands);
                continue;
            }

            Class<?> clazz = CLASS_LOADER.load(file.getAbsolutePath(), new FileInputStream(file));
            Method method = clazz.getDeclaredMethod("execute", Object.class, Object.class);

            if (method == null) {
                throw new RuntimeException("error loading method for " + file.getName());
            }

            String name = file.getName().substring(0, file.getName().indexOf("."));
            Command command = new Command(name, method);

            commands.add(command);
        }

        return commands;
    }
}
