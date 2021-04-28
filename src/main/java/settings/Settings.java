package settings;

import Midi.MidiDeviceManager;
import directory.DirTools;
import org.apache.log4j.Logger;
import javax.sound.midi.MidiUnavailableException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Settings {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private String configFile;
    private static Settings instance;

    private String midiDeviceName = "none";
    private String currentDrumKitName = "none";
    private String sampleStore;

    List<String> drumKits = new LinkedList<>();
    List<String> midiDevices = new LinkedList<>();

    private Settings() throws IOException, MidiUnavailableException {

    }

    public void init(String configDirectory) throws IOException, MidiUnavailableException {
        log.debug("load initial variables from "+configDirectory);

        configFile = configDirectory + "/midilauncher.properties";
        sampleStore = configDirectory + "/samplestore";
        log.debug("configDirectory=" + configDirectory);
        log.debug("configFile=" + configFile);
        log.debug("sampleStore=" + sampleStore);

        if (Files.notExists(Paths.get(configDirectory))) Files.createDirectory(Paths.get(configDirectory));
        if (Files.notExists(Paths.get(sampleStore))) Files.createDirectory(Paths.get(sampleStore));
        if (Files.notExists(Paths.get(configFile))) recreateAndFile(configFile);

        loadConfigFile(configFile);

        loadDrumKits();
        loadMidiDevices();
    }


    private void loadConfigFile(String configFile) throws IOException {
        log.info("Load config from " + configFile);
        Properties prop = new Properties();
        InputStream inputStream = new FileInputStream(configFile);
        prop.load(inputStream);

        if (prop.containsKey("midiDeviceName")) midiDeviceName = prop.getProperty("midiDeviceName");
        if (prop.containsKey("sampleStore")) sampleStore = prop.getProperty("sampleStore");
        if (prop.containsKey("drumKitName")) currentDrumKitName = prop.getProperty("drumKitName");
        log.debug("midiDeviceName is :" + midiDeviceName);
        log.debug("sampleStore is :" + sampleStore);
        log.debug("drumKitName is :" + currentDrumKitName);

        inputStream.close();
    }

    private void loadMidiDevices() throws MidiUnavailableException {
        log.debug("Load midi devices");
        midiDevices.addAll(MidiDeviceManager
                .getInstance()
                .getNamesOfMidiDevices());
    }

    private void recreateAndFile(String configFile) {
        log.debug("Recreate config file: " + configFile);
        try {
            if (Files.exists(Paths.get(configFile))) Files.delete(Paths.get(configFile));

            File file = new File(configFile);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(("midiDeviceName=" + midiDeviceName+System.lineSeparator()).getBytes());
            bos.write(("sampleStore=" + sampleStore+System.lineSeparator()).getBytes());
            bos.write(("drumKitName=" + currentDrumKitName+System.lineSeparator()).getBytes());
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings getInstance() throws IOException, MidiUnavailableException {
        if (instance == null) instance = new Settings();
        return instance;
    }


    public void loadDrumKits() {
        log.debug("load new drumkit");
        drumKits.clear();
        List<String> dirList = DirTools.getListOfSubdirs(sampleStore);
        drumKits.addAll(dirList);
    }

    public List<String> getDrumKits() {
        return drumKits;
    }

    public String getSampleStore() {
        return sampleStore;
    }

    public String getMidiDeviceName() {
        return midiDeviceName;
    }

    public void setMidiDeviceName(String deviceName) {
        midiDeviceName = deviceName;
        recreateAndFile(configFile);
    }

    public String getCurrentDrumKitName() {
        return currentDrumKitName;
    }

    public void setCurrentDrumKitName(String drumKitName) {
        log.debug("Set current drumkit to " + drumKitName);
        this.currentDrumKitName = drumKitName;
        recreateAndFile(configFile);
    }
}
