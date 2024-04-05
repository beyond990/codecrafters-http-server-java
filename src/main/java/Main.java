import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Logs from your program will appear here!");

        Options options = new Options();
        Option directoryOption = Option.builder("d").longOpt("directory")
                .argName("directory")
                .hasArg()
                // .required(true)
                .desc("Set directory for file storage").build();
        options.addOption(directoryOption);

        CommandLine cmd;
        CommandLineParser parser = new BasicParser();
        HelpFormatter helper = new HelpFormatter();

        Path directory = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("d")) {
                directory = Paths.get(cmd.getOptionValue("directory"));
                System.out.println("Directory set to " + directory.toString());
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helper.printHelp("Usage:", options);
            System.exit(0);
        }

        List<ClientHandler> clients = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();

        // if (directory == null) {
        // System.err.println("Directory option not set!");
        // System.exit(1);
        // }
        try {
            ServerSocket listner = new ServerSocket(4221);
            listner.setReuseAddress(true);
            while (!listner.isClosed()) {
                Socket client = listner.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");
                ClientHandler clientHandler = new ClientHandler(client, directory);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            }
            listner.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

    }

}
