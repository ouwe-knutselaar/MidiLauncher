package Midi;

import org.apache.log4j.Logger;
import settings.Settings;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import java.io.IOException;

public class MidiEventmanager {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    public MidiEventmanager() throws IOException, MidiUnavailableException {
        log.info("Start MidiEventmanager()");
        MidiDeviceManager midiDeviceManager = MidiDeviceManager.getInstance();
        Settings settings = Settings.getInstance();
        MidiDevice midiDevice = midiDeviceManager.getMidiDeviceByName(settings.getMidiDeviceName());
        midiDevice.open();
        Transmitter transmitter = midiDevice.getTransmitter();
        midiDevice.open();
        MidiEventReactor mer = new MidiEventReactor();
        transmitter.setReceiver(mer.getReceiver());
    }


}
