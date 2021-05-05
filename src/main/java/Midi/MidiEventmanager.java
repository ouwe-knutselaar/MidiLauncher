package Midi;

import org.apache.log4j.Logger;
import settings.Settings;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import java.io.IOException;

public class MidiEventmanager {

    private static final Logger log = Logger.getLogger(MidiEventmanager.class.getClass().getName());
    private String status = "uninitialized";
    private static MidiEventmanager instance;

    private MidiEventmanager() throws IOException, MidiUnavailableException {
        log.info("Start MidiEventmanager()");
        MidiDeviceManager midiDeviceManager = MidiDeviceManager.getInstance();
        Settings settings = Settings.getInstance();
        MidiDevice midiDevice = midiDeviceManager.getMidiDeviceByName(settings.getMidiDeviceName());
        midiDevice.open();
        if(midiDevice.getMaxTransmitters()>0) {
            Transmitter transmitter = midiDevice.getTransmitter();
            MidiEventReactor mer = new MidiEventReactor();
            transmitter.setReceiver(mer.getReceiver());
            status = "initialized and ready to receive messages";
        }else{
            log.warn("This midi device has no tranmitter");
            status = "unable to receive messages";
        }
    }

    public static MidiEventmanager getInstance() throws IOException, MidiUnavailableException {
        log.info("Get instance of MidiEventmanager");
        if(instance == null)instance = new MidiEventmanager();
        return instance;
    }

    public String getStatus(){
        return status;
    }
}
