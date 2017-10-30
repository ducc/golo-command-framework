package ovh.not.golocommandframework;

import org.eclipse.golo.compiler.GoloClassLoader;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class CommandLoader {
    List<Command> load(File dir) throws IOException, NoSuchMethodException {
        List<Command> commands = new ArrayList<>();

        if (dir.isFile()) {
            return commands;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return commands;
        }

        File executorFile = new File("src/main/resources/executor.golo");

        // the first arg serves as an identifier for compile errors, the next takes an input stream for the source
        Class<?> executorClazz = new GoloClassLoader()
                .load(executorFile.getAbsolutePath(), new FileInputStream(executorFile));

        // get the execute method which takes the source, event and args
        Method executorMethod = executorClazz.getDeclaredMethod("execute", Object.class, Object.class, Object.class);

        for (File file : files) {
            if (file.isDirectory()) {
                List<Command> subDirCommands = load(file);
                commands.addAll(subDirCommands);
                continue;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                String name = file.getName().substring(0, file.getName().indexOf("."));
                Command command = new Command(executorMethod, name, builder.toString());

                commands.add(command);
            }
        }

        return commands;
    }
}
