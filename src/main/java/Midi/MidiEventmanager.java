package Midi;

import org.apache.log4j.Logger;
import settings.Settings;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import java.io.IOException;
import java.util.Optional;

public class MidiEventmanager {

    private static final Logger log = Logger.getLogger(MidiEventmanager.class.getClass().getName());
    private String status = "uninitialized";
    private static MidiEventmanager instance;
    private Transmitter transmitter;
    private MidiDevice midiDevice;
    private MidiDeviceManager midiDeviceManager = MidiDeviceManager.getInstance();

    private MidiEventmanager() throws IOException, MidiUnavailableException {
        log.info("Start MidiEventmanager()");
        connectToMidiDevice();

    }

    public static MidiEventmanager getInstance() throws IOException, MidiUnavailableException {
        log.info("Get instance of MidiEventmanager");
        if (instance == null) instance = new MidiEventmanager();
        return instance;
    }

    private void connectToMidiDevice() throws MidiUnavailableException {
        Optional<MidiDevice> newMidiDevice = midiDeviceManager.getCurrentMidiDevice();
        if (newMidiDevice.isPresent()) {
            midiDevice = newMidiDevice.get();
            midiDevice.open();
            transmitter = midiDevice.getTransmitter();
            MidiEventReactor mer = new MidiEventReactor();
            transmitter.setReceiver(mer.getReceiver());
            status = "initialized and ready to receive messages";
            log.info("openend "+midiDevice.getDeviceInfo().getName());
        }
    }


    public String getStatus() {
        return status;
    }

    public Transmitter getTransmitter() {
        return transmitter;
    }
}
