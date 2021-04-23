import Midi.MidiDeviceManager;
import Midi.MidiEventReactor;

import javax.sound.midi.*;

public class Test {

    public static void main(String [] argv) throws MidiUnavailableException {

        MidiDeviceManager test = MidiDeviceManager.getInstance();

        test.getNamesOfMidiDevices().forEach(System.out::println);

        MidiDevice md = test.getMidiDeviceByName("MIDIIN2 (SAMSON Graphite 25)");
        md.open();

        Transmitter tm = md.getTransmitter();
        md.open();


        MidiEventReactor mer = new MidiEventReactor();

        tm.setReceiver(mer.getReceiver());

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

  }

