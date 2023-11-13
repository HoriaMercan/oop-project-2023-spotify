package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import commands.AbstractCommand;
import commands.AbstractCommand.CommandOutput;
import databases.MyDatabase;
import fileio.input.LibraryInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(LIBRARY_PATH), LibraryInput.class);

        ArrayNode outputs = objectMapper.createArrayNode();


        // My code starts here
        MyDatabase.getInstance().setSongsConvert(library.getSongs());
        MyDatabase.getInstance().setPodcastsConvert(library.getPodcasts());
        MyDatabase.getInstance().setUsersConvert(library.getUsers());
        MyDatabase.getInstance().setPublicPlaylists(new ArrayList<>());


        File file1 = new File(CheckerConstants.TESTS_PATH + filePath1);
        System.out.println(filePath1);
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<AbstractCommand.CommandInput> commandInput =
                objectMapper.readValue(file1, typeFactory.constructCollectionType(List.class, AbstractCommand.CommandInput.class));

        ArrayList<CommandOutput> commandOutputs = new ArrayList<>();
        for (AbstractCommand.CommandInput input: commandInput) {
//            System.out.println(input.getTimestamp());
            AbstractCommand command = input.getCommandFromInput();
            command.executeCommand();
            commandOutputs.add(command.getCommandOutput());
        }

        objectMapper.writerWithDefaultPrettyPrinter().
                forType(typeFactory.constructCollectionType(List.class, AbstractCommand.CommandOutput.class))
                .writeValue(new File(filePath2), commandOutputs);

    }
}
