package fr.axicer.furryattack.bootstrap;

import fr.axicer.furryattack.client.FAClient;
import fr.axicer.furryattack.client.control.EscapeListener;
import fr.axicer.furryattack.util.NumberUtils;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger("Bootstrap");

    public static void main(String[] args) {
        //create needed parameters from command line
        var widthOpt = new Option("w", "width", true, "screen width");
        var heightOpt = new Option("h", "height", true, "screen height");
        var fullscreenOpt = new Option("f", "fullscreen", false, "Launch as fullscreen (launched as windowed if not present)");
        var vsyncOpt = new Option("v", "vsync", false, "Use V-Sync (now V-Sync if not present)");
        var isServerOpt = new Option("s", "server", false, "launch as server (launch as client if not present)");

        //pack them as a set of options
        var options = new Options();
        options.addOption(widthOpt);
        options.addOption(heightOpt);
        options.addOption(isServerOpt);
        options.addOption(fullscreenOpt);
        options.addOption(vsyncOpt);

        //parse command
        var parser = new DefaultParser();
        var formatter = new HelpFormatter();
        CommandLine cmd = null;
        try{
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            formatter.printHelp("cmd", options);

            System.exit(1);
        }

        //parse and check for error
        var isServer = cmd.hasOption("s");
        var isFullscreen = cmd.hasOption("f");
        var isVsync = cmd.hasOption("v");
        var widthStr = (isServer || !cmd.hasOption("w")) ? "0" : cmd.getOptionValue("w");
        var heightStr = (isServer || !cmd.hasOption("h")) ? "0" : cmd.getOptionValue("h");

        var width = NumberUtils.parseInt(widthStr);
        var height = NumberUtils.parseInt(heightStr);
        if(NumberUtils.isAny(Integer.MIN_VALUE, width, height)){
            System.exit(2);
        }

        //start Boostrap
        start(width, height, isServer, isFullscreen, isVsync, 1);//TODO use screen ID from config
    }


    private static void start(int width, int height, boolean isServer, boolean fullscreen, boolean vsync, int screenID){
        if(isServer){
            LOGGER.debug("Starting server...");
            //TODO
        }else{
            final FAClient client = new FAClient();
            try {
                client.init(width, height, fullscreen, vsync, screenID);
            }catch (RuntimeException ex){
                LOGGER.error(ex.getMessage());
                System.exit(3);
            }

            FAClient.getEventManager().addListener(new EscapeListener(client));

            client.start();
            //nothing should be run under this line as they will only be called when the program shutdown
        }
    }

}
