package Midi;

import javax.sound.midi.*;
import java.util.List;

public class MidiEventReactor implements MidiDevice {

    private boolean isOpen = false;

    Receiver rc = new Receiver() {
        @Override
        public void send(MidiMessage message, long timeStamp) {

            ShortMessage sms = (ShortMessage)message;
            System.out.println("channel = "+sms.getChannel());
            System.out.println("command = "+sms.getCommand());
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
        isOpen=true;
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
