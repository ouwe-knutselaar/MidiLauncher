package audio;

import directory.DirTools;
import org.apache.log4j.Logger;
import settings.Settings;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SampleManager {

    private Logger log = Logger.getLogger(this.getClass().getName());
    private static final SampleManager Instance = new SampleManager();
    private final Map<Integer,WaveSample> sampleList = new HashMap<>();

    private SampleManager(){
        log.info("Load samplemanager");
    }

    public static SampleManager getInstance(){
        return Instance;
    }

    public void loadFromSampleDirectory(String sampleDirectory) throws IOException, MidiUnavailableException {
        log.debug("load drumkit from "+sampleDirectory);
        sampleList.clear();     // clear the list
        List<String> dirList = DirTools.getListOfSubdirs(sampleDirectory);
        for(String dir : dirList){
            List<String> fileList=DirTools.getListOfFiles(sampleDirectory+"/"+dir);
            if(fileList.size() == 0)continue;
            try {
                int note = Integer.parseInt(dir);
                String sampleFile = sampleDirectory+"/"+dir+"/"+fileList.get(0);
                log.debug("load sample from "+sampleFile);
                addSample(sampleFile,note);
            }catch (NumberFormatException e){
                continue;
            }

        }
    }


    public void addSample(String sampleFile,Integer note){
        log.debug("From drumkit load sample "+ sampleFile + " at note "+note);
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

    public List<String> getSamples(){
        return sampleList
                .values()
                .stream()
                .map(waveSample -> waveSample.getName())
                .collect(Collectors.toList());
    }

    public static String getNoteFromMidiNumber(int midiNote){
        String[] note_names = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        return note_names[midiNote % 12] + ((midiNote / 12) - 1);
    }

}
