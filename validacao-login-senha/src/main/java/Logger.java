import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Logger {

    private static final int tamanhoMaximo = 30;
    private static final int maximoHistoricoArquivos = 100;

    private static Calendar dataEHora = Calendar.getInstance();
    private static SimpleDateFormat formatoData = new SimpleDateFormat("HH:mm:ss");
    private static String dataFormatada = formatoData.format(dataEHora.getTime());

    private static Date data = new Date();
    private static SimpleDateFormat dataAtual = new SimpleDateFormat("yyyyMMdd");
    private static String dt = dataAtual.format(data);

    private static final String logDir = "logs" + File.separator;
    private static final String logFile = logDir + "HardwareInfo" + dt + ".log";

    public Logger() throws ParseException {
    }

    public static String getLogFile() {
        return logFile;
    }

    private static void checkLogRotation() throws IOException {
        long fileSize = new File(logFile).length();
        if (fileSize > tamanhoMaximo * 1024 * 1024) {
            rotateLogs();
        }
    }

    private static void rotateLogs() throws IOException {
        Date data = new Date();
        SimpleDateFormat dataAtual = new SimpleDateFormat("yyyyMMdd");
        String dt = dataAtual.format(data);

        String rotatedFileName = logDir + "HardwareInfo" + dt + ".log";
        File currentLogFile = new File(logFile);
        File rotatedFile = new File(rotatedFileName);
        currentLogFile.renameTo(rotatedFile);

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, false))) {
            writer.println("Log rotated at " + dataFormatada);
        }

        removeOldLogs();
    }

    private static void removeOldLogs() {
        File fileLogDir = new File(logDir);
        File[] files = fileLogDir.listFiles();

        if (files != null && files.length > maximoHistoricoArquivos) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            for (int i = 0; i < files.length - maximoHistoricoArquivos; i++) {
                files[i].delete();
            }
        }
    }

    public static synchronized void logDiscoInfo(List<DiscoT> discos) {
        for (DiscoT discoT : discos) {
            logInfo("Disco Info: \n" + discoT.toString(), DiscoT.class);
        }
    }

    public static void checkLogDirectory() {
        File logDirectory = new File(logDir);
        if (!logDirectory.exists()) {
            boolean created = logDirectory.mkdirs(); // Tenta criar o diretório e os subdiretórios

            if (!created) {
                System.err.println("Error creating the log directory.");
                return;
            }
        }
    }

    public static synchronized <T> void logInfo(String message, Class<T> clazz) {
        String logEntry = dataFormatada + " [" + clazz.getSimpleName() + "] " + message;
        try {
            checkLogDirectory();
            checkLogRotation();

            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
                writer.println(logEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void logWarning(String message, Class<T> clazz) {
        String logEntry = dataFormatada + " [" + clazz.getSimpleName() + "] " + message;
        try {
            checkLogRotation();
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
                writer.println(logEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}