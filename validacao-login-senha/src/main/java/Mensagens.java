public class Mensagens {
    private String boasVindas;
    private String adeus;


    public Mensagens() {
        this.boasVindas = """
                *******************************************************************************
                                
                ███████╗ █████╗ ███████╗████████╗████████╗ ██████╗ ████████╗███████╗███╗   ███╗
                ██╔════╝██╔══██╗██╔════╝╚══██╔══╝╚══██╔══╝██╔═══██╗╚══██╔══╝██╔════╝████╗ ████║
                █████╗  ███████║███████╗   ██║      ██║   ██║   ██║   ██║   █████╗  ██╔████╔██║
                ██╔══╝  ██╔══██║╚════██║   ██║      ██║   ██║   ██║   ██║   ██╔══╝  ██║╚██╔╝██║
                ██║     ██║  ██║███████║   ██║      ██║   ╚██████╔╝   ██║   ███████╗██║ ╚═╝ ██║
                ╚═╝     ╚═╝  ╚═╝╚══════╝   ╚═╝      ╚═╝    ╚═════╝    ╚═╝   ╚══════╝╚═╝     ╚═╝
                                                                                              \s
                                
                
                *************************** Bem vindo ao FastTotem! ***************************
                """;
        this.adeus = """
                ********************************   Até Logo!!   ********************************
                """;;
    }

    public String getBoasVindas() {
        return boasVindas;
    }

    public String getAdeus() {
        return adeus;
    }
}
