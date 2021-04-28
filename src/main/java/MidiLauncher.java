import audio.SampleManager;
import org.apache.log4j.Logger;
import settings.Settings;
import telnet.TelnetServer;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class MidiLauncher {

    private static Logger log = Logger.getLogger("MidiLancher");
    private SampleManager sampleManager;
    private Settings settings;

    public static void main(String [] argv){

        if(argv.length == 0 ){
            log.error("No config directory as parameter");
            System.exit(1);
        }

        MidiLauncher midiLauncher = new MidiLauncher(argv[0]);
        try {
            midiLauncher.init();
            midiLauncher.start();
        } catch (MidiUnavailableException | IOException | UnsupportedAudioFileException e) {
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


    private void init() throws MidiUnavailableException, IOException, UnsupportedAudioFileException {



        /*MidiDeviceManager test = MidiDeviceManager.getInstance();
        test.getNamesOfMidiDevices().forEach(System.out::println);
        MidiDevice md = test.getMidiDeviceByName("MIDIIN2 (SAMSON Graphite 25)");
        md.open();
        Transmitter tm = md.getTransmitter();
        md.open();
        MidiEventReactor mer = new MidiEventReactor();
        tm.setReceiver(mer.getReceiver());*/
    }

    private void start() throws IOException, MidiUnavailableException {

        TelnetServer telnetServer = new TelnetServer();
        Thread telnetServerThread = new Thread(telnetServer);
        telnetServerThread.start();

        log.info("Current drumikit "+settings.getCurrentDrumKitName());

        settings.getDrumKits().forEach(drumKit -> log.info("available drum :"+drumKit));
        sampleManager.loadFromSampleDirectory(settings.getSampleStore()+"/"+settings.getCurrentDrumKitName());


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
