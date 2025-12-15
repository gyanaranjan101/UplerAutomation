package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenRecorderUtil {

    private static Process recordingProcess;
    private static BufferedWriter ffmpegWriter;
    private static String videoPath;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static void startRecording(String scenarioName) {

        try {
            String safeName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");

            String timestamp = LocalDateTime.now().format(FORMATTER);

            File videoDir = new File("target/videos");
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }

            videoPath = videoDir.getAbsolutePath()
                    + File.separator
                    + safeName + "_" + timestamp + ".mp4";

            String ffmpegPath = "C:\\ffmpeg\\bin\\ffmpeg.exe";

            ProcessBuilder builder = new ProcessBuilder(
                    ffmpegPath,
                    "-y",
                    "-f", "gdigrab",
                    "-framerate", "20",
                    "-i", "desktop",
                    "-vcodec", "libx264",
                    "-preset", "ultrafast",
                    "-pix_fmt", "yuv420p",
                    videoPath
            );

            builder.redirectErrorStream(true);
            recordingProcess = builder.start();

            ffmpegWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            recordingProcess.getOutputStream(),
                            StandardCharsets.UTF_8
                    )
            );

            System.out.println("🎥 Screen recording started: " + videoPath);

        } catch (Exception e) {
            System.err.println("⚠️ Screen recording failed to start");
            e.printStackTrace();
        }
    }

    public static String stopRecording() {

        try {
            if (ffmpegWriter != null) {
                ffmpegWriter.write("q");
                ffmpegWriter.newLine();
                ffmpegWriter.flush();
                ffmpegWriter.close();
            }

            if (recordingProcess != null) {
                recordingProcess.waitFor();
                recordingProcess.destroy();
            }

            System.out.println("🎬 Screen recording stopped");

        } catch (Exception e) {
            System.err.println("⚠️ Failed to stop screen recording");
            e.printStackTrace();
        }

        return videoPath;
    }
}
