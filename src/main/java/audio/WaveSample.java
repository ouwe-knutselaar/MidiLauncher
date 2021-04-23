package audio;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WaveSample {

    private byte[] sampleInBytes;
    private AudioFileFormat aff;

    public void LoadSample(String filename) throws IOException, UnsupportedAudioFileException {
        System.out.println("load sample "+filename);
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

}
