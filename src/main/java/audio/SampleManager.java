package audio;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SampleManager {

    private static final SampleManager Instance = new SampleManager();
    private final Map<Integer,WaveSample> sampleList = new HashMap<>();

    private SampleManager(){

    }

    public static SampleManager getInstance(){
        return Instance;
    }


    public void addSample(String sampleFile,Integer note){
        try {
            WaveSample temp = new WaveSample();
            temp.LoadSample(sampleFile);
            sampleList.put(note,temp);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void playSample(int sampleNumber){
        try {
            if(sampleList.containsKey(sampleNumber))sampleList.get(sampleNumber).playSample();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static String getNoteFromMidiNumber(int midiNote){
        String[] note_names = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        return note_names[midiNote % 12] + ((midiNote / 12) - 1);
    }

}
