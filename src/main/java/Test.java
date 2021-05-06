import Midi.MidiDeviceManager;
import Midi.MidiEventReactor;
import settings.Settings;

import javax.sound.midi.*;
import java.io.IOException;

public class Test {

    public static void main(String [] argv) throws IOException, MidiUnavailableException {

        Settings settings = Settings.getInstance();
        settings.init(argv[0]);

        MidiDeviceManager midiDeviceManager = MidiDeviceManager.getInstance();
        midiDeviceManager.setCurrentMidiDevice(settings.getMidiDeviceName());
        MidiEventReactor midiEventReactor = new MidiEventReactor();
        MidiDevice midiDevice = midiDeviceManager.getCurrentMidiDevice().get();
        midiDevice.open();

        Transmitter transmitter = midiDevice.getTransmitter();
        transmitter.setReceiver(midiEventReactor.getReceiver());

        while(true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

  }

