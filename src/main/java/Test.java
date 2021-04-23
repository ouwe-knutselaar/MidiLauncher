import Midi.MidiDeviceManager;
import Midi.MidiEventReactor;
import audio.SampleManager;
import audio.WaveSample;

import javax.sound.midi.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Test {

    public static void main(String [] argv) throws MidiUnavailableException, IOException, UnsupportedAudioFileException, LineUnavailableException {

        /*WaveSample wv = new WaveSample();
        wv.LoadSample("D:\\erwin\\c5.wav");
        wv.playSample();
*/
        SampleManager sm = SampleManager.getInstance();
        sm.addSample("D:\\erwin\\bd1.wav",60);
        sm.addSample("D:\\erwin\\sn1.wav",62);

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

    public void midi(){

    }

  }

