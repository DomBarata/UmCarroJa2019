import static java.lang.System.out;

public class UmCarroJaMVC {
    public static void main(String[] args) {

        UmCarroJaModel model = new UmCarroJaModel();
        model.createData();
        if(model == null) { out.println("ERRO INICIALIZACAO"); System.exit(-1); }
        UmCarroJaView view = new UmCarroJaView();
        UmCarroJaController control = new UmCarroJaController();
        control.setModel(model);
        control.setView(view);
        control.start();
        System.exit(0);
    }
}
