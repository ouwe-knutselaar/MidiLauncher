package telnet;


import Midi.MidiDeviceManager;
import audio.SampleManager;
import org.apache.log4j.Logger;
import settings.Settings;

import javax.sound.midi.MidiUnavailableException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.*;

public class TelnetServer implements Runnable {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private boolean loop = true;
    ServerSocket serverSocket;
    Socket clientSocket;
    BufferedReader bf;
    PrintWriter pw;
    Settings settings;
    MidiDeviceManager midiDeviceManager;
    SampleManager sampleManager;

    public TelnetServer() throws IOException, MidiUnavailableException {
        settings = Settings.getInstance();
        midiDeviceManager = MidiDeviceManager.getInstance();
        sampleManager = SampleManager.getInstance();
        log.info("Start telnet server at port "+settings.getIntTcpPort());
        serverSocket = new ServerSocket(settings.getIntTcpPort());
    }


    @Override
    public void run() {
        log.info("Start telnet thread");
        while (loop) {
            try {
                clientSocket = serverSocket.accept();
                HandleSession(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void HandleSession(Socket clientSocket) throws IOException {
        log.info("New session from " + clientSocket.getInetAddress().getHostAddress());
        bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        pw = new PrintWriter(clientSocket.getOutputStream(), true);
        MainScreen();
        pw.close();
        bf.close();
        clientSocket.close();
        log.info("Session ended");
    }

    private void MainScreen() throws IOException {
        boolean loop = true;
        while(loop) {
            pw.println("");
            pw.println("MidiLauncher");
            pw.println("Midi device '" + settings.getMidiDeviceName() + "'");
            pw.println("Drum kit    '" + settings.getCurrentDrumKitName() + "'");
            pw.println(" 1 Select midi device");
            pw.println(" 2 Select drumkit");
            pw.println(" 3 Show overview");
            pw.println(" q quit");
            pw.println("choice:>");
            String response = bf.readLine();
            if (response.equals("q")) loop = false;
            if (response.equals("1")) settings.setMidiDeviceName(selectMidiDevice());
            if (response.equals("2")) {
                settings.setCurrentDrumKitName(selectDrumKit());
                sampleManager.loadFromSampleDirectory(settings.getSampleStore() + "/" + settings.getCurrentDrumKitName());
            }
            if (response.equals("3")) showOverview();
        }
        pw.println("Session ended");
    }

    private void showOverview() {
        pw.println("");
        pw.println("Current drumkit is:" + settings.getCurrentDrumKitName());
        pw.println("Used samples:");
        sampleManager.getSamples().stream().map(fullPath -> Paths.get(fullPath).getFileName()).forEach(pw::println);
        pw.println("current midi device is:"+settings.getMidiDeviceName());
    }

    private String selectDrumKit() throws IOException {
        pw.println("");
        pw.println("Current drumkit is " + settings.getCurrentDrumKitName());
        while (true) {
            pw.println("select drumkit");
            Map<String, String> workMap = getMapOfDevices(settings.getDrumKits());
            workMap.forEach((K, V) -> pw.printf(" %s %s%s", K, V, System.lineSeparator()));
            pw.println(" q quit session");
            pw.println("choice:>");
            String response = bf.readLine();
            if (response.equals("q")) return settings.getCurrentDrumKitName();
            if (workMap.containsKey(response)) return workMap.get(response);
            pw.println("invalid choice " + response);
        }
    }

    private String selectMidiDevice() throws IOException {
        pw.println("");
        pw.println("current device is "+settings.getMidiDeviceName());
        while (true) {
            pw.println("select midi device");
            Map<String, String> workMap = getMapOfDevices(midiDeviceManager.getNamesOfMidiDevices());
            workMap.forEach((K, V) -> pw.printf(" %s %s%s", K, V, System.lineSeparator()));
            pw.println(" q quit");
            pw.println("choice:>");
            String response = bf.readLine();
            if (response.equals("q")) return settings.getMidiDeviceName();
            if (workMap.containsKey(response)) return workMap.get(response);
            pw.println("invalid choice " + response);
        }
    }

    private Map<String, String> getMapOfDevices(List<String> inputList) {
        Map<String, String> workMap = new HashMap<>();
        for (int tel = 0; tel < inputList.size(); tel++) {
            workMap.put("" + tel, inputList.get(tel));
        }
        return workMap;
    }

}

