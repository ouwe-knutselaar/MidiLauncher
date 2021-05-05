import Midi.MidiDeviceManager;
import Midi.MidiEventReactor;
import Midi.MidiEventmanager;
import audio.SampleManager;
import audio.WaveSample;
import telnet.TelnetServer;
import telnet.TelnetServerTest;

import javax.sound.midi.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Test {

    public static void main(String [] argv) throws IOException, MidiUnavailableException {

        TelnetServerTest telnetServer = new TelnetServerTest();
        Thread telnetServerThread = new Thread(telnetServer);
        telnetServerThread.start();


        while(true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

  }

