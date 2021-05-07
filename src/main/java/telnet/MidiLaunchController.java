package telnet;

import java.util.List;

public interface MidiLaunchController {


    List<String> getMidiDevices();
    void setMidiDevice(String name);
    String getCurrentMidiDevice();

    List<String> getDrumKits();
    void setDrumKit(String name);
    String getCurrentDrumKit();

}
