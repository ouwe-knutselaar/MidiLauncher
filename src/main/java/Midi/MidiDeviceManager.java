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

    public void setCurrentMidiDevice(String name) throws MidiUnavailableException {
        if(currentMidiDevice != null)currentMidiDevice.close();
        if(midiDeviceList.containsKey(name)){
            currentMidiDevice = midiDeviceList.get(name);
            currentMidiDevice.open();
            log.info("Midi device is set to "+ name);
        }else{
            log.info("Cannot conect to "+ name);
        }
    }

    public Optional<MidiDevice> getCurrentMidiDevice(){
        return Optional.ofNullable(currentMidiDevice);
    }

    public void addMidiEventReactor(MidiDevice reactor) throws MidiUnavailableException {
        log.info("Add reactor to midi device "+currentMidiDevice.getDeviceInfo().getName());
        currentMidiDevice.getTransmitter().setReceiver(reactor.getReceiver());

    }
}
