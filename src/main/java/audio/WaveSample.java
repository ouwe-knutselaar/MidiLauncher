package audio;

import org.apache.log4j.Logger;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WaveSample {

    private Logger log = Logger.getLogger(this.getClass().getName());
    private byte[] sampleInBytes;
    private AudioFileFormat aff;
    private String name;

    public void LoadSample(String filename) throws IOException, UnsupportedAudioFileException {
        log.debug("load sample in to memory "+filename);
        name = filename;
        sampleInBytes = Files.readAllBytes(Paths.get(filename));
        aff = AudioSystem.getAudioFileFormat(new ByteArrayInputStream(sampleInBytes));
    }

    public void playSample() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(sampleInBytes));
        AudioFormat af = ais.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, af);
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(ais);
        clip.start();
    }

    public String getName(){
        return name;
    }

}
