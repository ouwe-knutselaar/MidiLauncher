import Midi.MidiEventmanager;
import audio.SampleManager;
import org.apache.log4j.Logger;
import settings.Settings;
import telnet.TelnetServer;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

public class MidiLauncher {

    private static final Logger log = Logger.getLogger("MidiLancher");
    private SampleManager sampleManager;
    private Settings settings;

    public static void main(String [] argv){

        if(argv.length == 0 ){
            log.error("No config directory as parameter");
            System.exit(1);
        }

        MidiLauncher midiLauncher = new MidiLauncher(argv[0]);
        try {

            midiLauncher.start();
        } catch (MidiUnavailableException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public MidiLauncher(String configDirectory){
        configDirectory = configDirectory.replace('\\','/');
        log.info("Start Midilauncher");
        try{
        settings = Settings.getInstance();
        settings.init(configDirectory);
        sampleManager = SampleManager.getInstance();
    }catch (IOException | MidiUnavailableException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void start() throws IOException, MidiUnavailableException {

        TelnetServer telnetServer = new TelnetServer();
        Thread telnetServerThread = new Thread(telnetServer);
        telnetServerThread.start();

        settings.getDrumKits().forEach(drumKit -> log.info("available drum :"+drumKit));
        log.info("Current drumikit "+settings.getCurrentDrumKitName());

        sampleManager.loadFromSampleDirectory(settings.getSampleStore()+"/"+settings.getCurrentDrumKitName());
        MidiEventmanager midiEventmanager = new MidiEventmanager();

        while(true){
            log.info("Start the loop");
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
