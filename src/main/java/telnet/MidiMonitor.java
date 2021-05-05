package telnet;

import org.apache.log4j.Logger;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class MidiMonitor implements MidiDevice {

    private static MidiMonitor instance;
    private boolean doWrite = false;
    private Writer defaultOut;
    private final Logger log = Logger.getLogger(this.getClass().getName());


    public static MidiMonitor getInstance(Writer defaultOut) {
        if (instance == null) instance = new MidiMonitor();
        instance.defaultOut = defaultOut;
        return instance;
    }

    private Receiver rc = new Receiver() {

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (doWrite) {
                try {
                    defaultOut.write("- " + message.getMessage()[0] + " " + message.getMessage()[1] + " " + message.getMessage()[2]);
                    log.info("- " + message.getMessage()[0] + " " + message.getMessage()[1] + " " + message.getMessage()[2]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void close() {

        }
    };

    public void enableWrite() {
        log.info("Enable midi monitoring");
        doWrite = true;
    }

    public void disableWrite() {
        log.info("Disable midi monitoring");
        doWrite = false;
    }

    @Override
    public Info getDeviceInfo() {
        return null;
    }

    @Override
    public void open() throws MidiUnavailableException {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public long getMicrosecondPosition() {
        return 0;
    }

    @Override
    public int getMaxReceivers() {
        return 0;
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
