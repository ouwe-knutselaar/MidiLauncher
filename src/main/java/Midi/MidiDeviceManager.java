package Midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MidiDeviceManager {

    private static MidiDeviceManager Instance = null;
    private MidiDevice.Info[] midiDevices;


    private MidiDeviceManager(){
        midiDevices =MidiSystem.getMidiDeviceInfo();

    }

    public static MidiDeviceManager getInstance(){
        if(Instance == null)Instance = new MidiDeviceManager();
        return Instance;
    }

    public List<String> getNamesOfMidiDevices(){
       return Arrays.stream(midiDevices).map(device -> device.getName()).collect(Collectors.toList());
    }

    public MidiDevice getMidiDeviceByName(String name){
        return null;
    }
}
