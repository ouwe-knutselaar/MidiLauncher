package Midi;

import audio.SampleManager;
import org.apache.log4j.Logger;

import javax.sound.midi.*;
import java.util.List;

public class MidiEventReactor implements MidiDevice {

    private boolean isOpen = true;
    private final SampleManager sm;
    private final Logger log = Logger.getLogger(this.getClass().getName());


    public MidiEventReactor() {
        log.info("Load MidiEventReactor()");
        sm = SampleManager.getInstance();
    }

    private Receiver rc = new Receiver() {

        @Override
        public void send(MidiMessage message, long timeStamp) {

            ShortMessage sms = (ShortMessage) message;
            log.info("- cmd" + sms.getCommand() + " d1:"  + sms.getData1() + " d2:" + sms.getData2()+" ch"+sms.getChannel());
            if (sms.getCommand() == 144) {
                sm.playSample(sms.getData1());
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
    public void open() {
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
    public Receiver getReceiver()  {
        return rc;
    }

    @Override
    public List<Receiver> getReceivers() {
        return null;
    }

    @Override
    public Transmitter getTransmitter() {
        return null;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return null;
    }
}
