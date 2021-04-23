package Midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MidiDeviceManager {

    private static MidiDevice.Info[] midiDevices;
    
    private final Map<String,MidiDevice> midiDeviceList = new HashMap<>();
    private static MidiDeviceManager instance = null;

    private MidiDeviceManager() throws MidiUnavailableException {
        midiDevices = MidiSystem.getMidiDeviceInfo();
        for(MidiDevice.Info midiDevInfo : midiDevices)midiDeviceList.put(midiDevInfo.getName(),MidiSystem.getMidiDevice(midiDevInfo));

    }

    public static MidiDeviceManager getInstance() throws MidiUnavailableException {
        if(instance == null) instance = new MidiDeviceManager();
        return instance;
    }

    public List<String> getNamesOfMidiDevices(){
       return Arrays.stream(midiDevices).map(MidiDevice.Info::getName).collect(Collectors.toList());
    }

    public MidiDevice getMidiDeviceByName(String name){
        return midiDeviceList.get(name);
    }
}
