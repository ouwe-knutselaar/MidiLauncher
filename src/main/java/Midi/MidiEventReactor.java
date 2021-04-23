package Midi;

import audio.SampleManager;
import audio.WaveSample;

import javax.sound.midi.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

public class MidiEventReactor implements MidiDevice {

    private boolean isOpen = false;
    private final SampleManager sm;


    public MidiEventReactor() throws IOException, UnsupportedAudioFileException {
        sm = SampleManager.getInstance();
    }

    Receiver rc = new Receiver() {

        @Override
        public void send(MidiMessage message, long timeStamp) {

            ShortMessage sms = (ShortMessage) message;
            if (sms.getCommand() == 144) {
                sm.playSample(sms.getData1());

                //System.out.println("channel = " + sms.getChannel());
                //System.out.println("command = " + sms.getCommand());
                //System.out.println("data1   = " + sms.getData1());
                //System.out.println("data2   = " + sms.getData2());
            }
        }

        @Override
        public void close() {

        }
    };

    @Override
    public Info getDeviceInfo() {
        return null;
    }

    @Override
    public void open() throws MidiUnavailableException {
        isOpen = true;
    }

    @Override
    public void close() {
        isOpen = false;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public long getMicrosecondPosition() {
        return 0;
    }

    @Override
    public int getMaxReceivers() {
        return 1;
    }

    @Override
    public int getMaxTransmitters() {
        return 0;
    }

    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
        return rc;
    }

    @Override
    public List<Receiver> getReceivers() {
        return null;
    }

    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
        return null;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return null;
    }
}
