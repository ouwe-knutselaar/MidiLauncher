package telnet;


import Midi.MidiDeviceManager;
import org.apache.log4j.Logger;
import settings.Settings;

import javax.sound.midi.MidiUnavailableException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TelnetServer implements Runnable {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    boolean session = true;
    private final int port = 3030;
    ServerSocket serverSocket;
    Socket clientSocket;
    BufferedReader bf;
    PrintWriter pw;
    Settings settings;
    MidiDeviceManager midiDeviceManager;

    public TelnetServer() throws IOException, MidiUnavailableException {
        log.info("Start telnet server");
        serverSocket = new ServerSocket(port);
        settings = Settings.getInstance();
        midiDeviceManager = MidiDeviceManager.getInstance();
    }


    @Override
    public void run() {
        log.info("Start telnet thread");
        while (true) {

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
        while (session) {
            MainScreen();
        }
    }

    private void MainScreen() throws IOException {
        pw.println("");
        pw.println("MidiLauncher");
        pw.println("Midi device " + settings.getMidiDeviceName());
        pw.println("Drum kit " + settings.getCurrentDrumKitName());
        pw.println(" 1 Select midi device");
        pw.println(" 2 Select drumkit");
        pw.println(" q quit");
        pw.print("choice:>");
        String response = bf.readLine();
        if (response.equals("q")) return;
        if (response.equals("1")) settings.setMidiDeviceName(selectMidiDevice());
        if (response.equals("2")) settings.setCurrentDrumKitName(selectDrumKit());
    }

    private String selectDrumKit() throws IOException {
        pw.println("");
        while (true) {
            pw.println("select drumkit");
            Map<String, String> workMap = getMapOfDevices(settings.getDrumKits());
            workMap.forEach((K, V) -> pw.printf("%s %s%s", K, V, System.lineSeparator()));
            pw.println(" q quit");
            String response = bf.readLine();
            if (response.equals("q")) return settings.getCurrentDrumKitName();
            if (workMap.containsKey(response)) return workMap.get(response);
            pw.println("invalid choice " + response);
        }

    }

    private String selectMidiDevice() throws IOException {
        pw.println("");
        while (true) {
            pw.println("select midi device");
            Map<String, String> workMap = getMapOfDevices(midiDeviceManager.getNamesOfMidiDevices());
            workMap.forEach((K, V) -> pw.printf("%s %s%s", K, V, System.lineSeparator()));
            pw.println(" q quit");
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

