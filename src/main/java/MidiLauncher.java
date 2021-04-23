import Midi.MidiDeviceManager;
import Midi.MidiEventReactor;
import audio.SampleManager;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class MidiLauncher {

    SampleManager sm = SampleManager.getInstance();

    public static void main(String [] argv){
        MidiLauncher midiLauncher = new MidiLauncher();
        try {
            midiLauncher.init();
            midiLauncher.start();
        } catch (MidiUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }


    }


    private void init() throws MidiUnavailableException, IOException, UnsupportedAudioFileException {
        sm.addSample("D:\\erwin\\bd1.wav",60);
        sm.addSample("D:\\erwin\\sn1.wav",62);
        sm.addSample("D:\\erwin\\chh1.wav",64);
        sm.addSample("D:\\erwin\\cc1.wav",65);
        MidiDeviceManager test = MidiDeviceManager.getInstance();

        test.getNamesOfMidiDevices().forEach(System.out::println);

        MidiDevice md = test.getMidiDeviceByName("MIDIIN2 (SAMSON Graphite 25)");
        md.open();

        Transmitter tm = md.getTransmitter();
        md.open();


        MidiEventReactor mer = new MidiEventReactor();

        tm.setReceiver(mer.getReceiver());
    }

    private void start() {

        while(true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
