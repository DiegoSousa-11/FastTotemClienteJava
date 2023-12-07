import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.util.Conversor;
import conexao.Conexao;
import org.springframework.jdbc.core.JdbcTemplate;
import oshi.hardware.HardwareAbstractionLayer;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.awt.Color.red;

public class MaquinaT {
    private Sistema sistema;
    private String sistemaOperacional;
    private String fabricante;
    private String nomeProcessador;
    private Long capacidadeRam;
    private Long capacidadeDisco;
    private Long tempoDeAtividade;
    private Integer fkTotem;

    private final Conexao conexao = new Conexao();
    private final JdbcTemplate con = conexao.getConexaoDoBanco();
    private final JdbcTemplate conSqlServer = conexao.getConexaoSqlServer();
    private static ZoneId zoneSaoPaulo = ZoneId.of("America/Sao_Paulo");

    public MaquinaT() {
        this.sistema = new Sistema();
        Processador processador = new Processador();
        Memoria memoria = new Memoria();
        DiscoGrupo grupoDeDiscos = new DiscoGrupo();


        this.sistemaOperacional = sistema.getSistemaOperacional();
        this.fabricante = sistema.getFabricante();
        this.nomeProcessador = processador.getNome();
        this.capacidadeRam = memoria.getTotal();
        this.capacidadeDisco = grupoDeDiscos.getTamanhoTotal();
        this.tempoDeAtividade = sistema.getTempoDeAtividade();
    }

    public void inserirDadosSistema() {
        try {
            conSqlServer.update("INSERT INTO infoMaquina " +
                    "(sistemaOperacional, fabricante, nomeProcessador, " +
                    "capacidadeRam, capacidadeDisco, fkTotem) " +
                    "VALUES (?,?,?,?,?,?)", sistemaOperacional, fabricante, nomeProcessador, capacidadeRam, capacidadeDisco, fkTotem);
            con.update("INSERT INTO infoMaquina " +
                    "(sistemaOperacional, fabricante, nomeProcessador, " +
                    "capacidadeRam, capacidadeDisco, fkTotem) " +
                    "VALUES (?,?,?,?,?,1)", sistemaOperacional, fabricante, nomeProcessador, capacidadeRam, capacidadeDisco);

            System.out.println("Dados do sistema inseridos!");
        } catch (Exception e) {
            Logger.logInfo(String.format("Erro ao inserir dados do sistema - %s", e), MaquinaT.class);
            e.printStackTrace();
        }
    }
    public void inserirTempoDeAtividade() {
        try {
            conSqlServer.update("INSERT INTO captura (valor, tipo, dataHora, fkTotem) VALUES (?,?,?,?)",
                    tempoDeAtividade, String.valueOf(TipoEnum.TEMPO_ATIVIDADE), LocalDateTime.now(zoneSaoPaulo), fkTotem);
            System.out.println("Captura realizada!");
        } catch (Exception e) {
            Logger.logInfo(String.format("Erro ao inserir tempo de atividade - %s", e), MaquinaT.class);
            e.printStackTrace();
        }

        try {
            con.update("INSERT INTO captura (valor, tipo, dataHora, fkTotem) VALUES (?,?,?,1)",
                    tempoDeAtividade, String.valueOf(TipoEnum.TEMPO_ATIVIDADE), LocalDateTime.now(zoneSaoPaulo));

        } catch (Exception e) {
            Logger.logInfo(String.format("Erro ao inserir tempo de atividade (MySQL Local) - %s", e), MaquinaT.class);
            e.printStackTrace();
        }

    }

    public Sistema getSistema() {
        return sistema;
    }

    public String getSistemaOperacional() {
        return getSistema().getSistemaOperacional();
    }

    public Integer getArquitetura() {
        return sistema.getArquitetura();
    }

    public Integer getFkTotem() {
        return fkTotem;
    }

    public void setFkTotem(Integer fkTotem) {
        this.fkTotem = fkTotem;
    }

    public void monitorarTempoAtividade() {
        while (true) {
            if (tempoDeAtividade >= 80.0) {
                Logger.logWarning("⚠️ [ALERTA] Totem em muito tempo de atividade",  MaquinaT.class);
            } else if (tempoDeAtividade >= 95.0) {
                Logger.logWarning("❌" + "[SEVERO] É necessário Reiniciar o Totem ", MaquinaT.class);
            } else {
                Logger.logInfo("✅" + "[INFO] Maquina: \n" + this, MaquinaT.class);
            }
            try {
                Thread.sleep(1800000);// Aguarda 2 minutos antes de verificar novamente
            } catch (InterruptedException e) {
                Logger.logInfo("Erro no monitoramento do tempo de Atividade da Maquina.\" " + e, Componente.class);
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sistema operacional: ").append(this.sistemaOperacional).append("\n");
//        sb.append("Fabricante: ").append(this.fabricante).append("\n");
//        sb.append("Arquitetura: ").append(this.arquitetura).append("bits\n");
//      sb.append("Inicializado: ").append(this.getInicializado()).append("\n");
        sb.append("Tempo de atividade: ").append(Conversor.formatarSegundosDecorridos(this.sistema.getTempoDeAtividade())).append("\n");
//        sb.append("Permissões: ").append("Executando como ").append(this.getPermissao() ? "root" : "usuário padrão").append("\n");
        return sb.toString();
    }
}