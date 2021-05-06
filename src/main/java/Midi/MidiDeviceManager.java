package Midi;

import org.apache.log4j.Logger;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.util.*;
import java.util.stream.Collectors;

public class MidiDeviceManager {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final Map<String, MidiDevice> midiDeviceList = new HashMap<>();
    private static MidiDeviceManager instance = null;
    private MidiDevice currentMidiDevice;

    private MidiDeviceManager() throws MidiUnavailableException {
        log.debug("Load MidiDeviceManager()");
        MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();

        for (MidiDevice.Info midiDevInfo : midiDevices) {
            log.debug("Examine "+midiDevInfo.getName()+" "+midiDevInfo.getDescription());
            MidiDevice midiDevice = MidiSystem.getMidiDevice(midiDevInfo);
            if (midiDevice.getMaxTransmitters() != 0) {
                midiDeviceList.put(midiDevInfo.getName(), MidiSystem.getMidiDevice(midiDevInfo));
                log.info("Add midi transmitter "+midiDevInfo.getName()+" "+midiDevInfo.getDescription());
            }
        }

    }

    public static MidiDeviceManager getInstance() throws MidiUnavailableException {
        if (instance == null) instance = new MidiDeviceManager();
        return instance;
    }

    public List<String> getNamesOfMidiDevices() {
        return midiDeviceList.entrySet()
                .stream()
                .map(V -> V.getValue()
                        .getDeviceInfo()
                        .getName())
                .collect(Collectors.toList());
    }

    public void setCurrentMidiDevice(String name){
        if(midiDeviceList.containsKey(name)){
            currentMidiDevice = midiDeviceList.get(name);
            log.info("Midi device is set to "+ name);
        }
    }

    public Optional<MidiDevice> getCurrentMidiDevice(){
        return Optional.ofNullable(currentMidiDevice);
    }
}
