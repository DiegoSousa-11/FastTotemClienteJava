import com.github.britooo.looca.api.group.sistema.Sistema;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ProgramarDesligamento {
    private Scanner in = new Scanner(System.in);
    public void programar() {
        System.out.println("Que horas você deseja desligar seu totem hoje? Ex: 00:00");

        String horario = in.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime horaFormatada = null;

        try {
            horaFormatada = LocalTime.parse(horario, formatter);
        } catch(DateTimeParseException dateTimeParseException) {
            System.out.println("Formato incorreto! Tente novamente...");
            programar();
        }

        if(horaFormatada != null) {
            LocalDateTime agora = LocalDateTime.now();

            LocalDateTime horaFornecidaHoje = agora.withHour(horaFormatada.getHour()).withMinute(horaFormatada.getMinute());

            if (horaFornecidaHoje.isBefore(agora)) {
                System.out.println("Esse horário já passou! Tente novamente...");
                programar();
            } else {
                Duration diferenca = Duration.between(agora, horaFornecidaHoje);

                long diferencaEmMilissegundos = diferenca.toSeconds();

                desligar(diferencaEmMilissegundos);
            }
        }
    }

    public void desligar(Long tempoEmSegundos) {
        Sistema sistema = new Sistema();

        if (sistema.getSistemaOperacional().toLowerCase().equals("windows")){
            try {
                Process process = Runtime.getRuntime().exec("shutdown -s -t " + tempoEmSegundos);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (sistema.getSistemaOperacional().toLowerCase().equals("linux")){
            try {
                Process process = Runtime.getRuntime().exec("sudo shutdown -h " + tempoEmSegundos/60);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
