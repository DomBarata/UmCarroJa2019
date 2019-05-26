import java.util.List;
import java.util.Map;

import static java.lang.System.*;


public class UmCarroJaController {
    private UmCarroJaModel model;
    private UmCarroJaView view;
    private String nif;

    public void setModel(UmCarroJaModel model) {
        this.model = model;
    }

    public void setView(UmCarroJaView view) {
        this.view = view;
    }

    public void start() {
        String password;
        int errado = 0;
        boolean flag = true;
        while (flag) {
            this.nif = this.view.userLogin();
            switch (this.model.existeNif(nif)) {
                case 1: while (errado < 5) {
                            password = this.view.passwordLogin();
                            if (this.model.checkProprietarioPassword(nif, password)){
                                proprietarioControl();
                                flag = false;
                                errado = 5;
                            }else{
                                errado++;
                                this.view.wrongPassword(errado);
                                if (errado == 5) {
                                    this.view.wrongPassword();
                                    exit(0);
                                }
                            }
                        }
                        break;
                case 2: while (errado < 5) {
                            password = this.view.passwordLogin();
                            if (this.model.checkClientePassword(nif, password)) {
                                clienteControl();
                                flag = false;
                                errado = 5;
                            }else{
                                errado++;
                                this.view.wrongPassword(errado);
                                if (errado == 5) {
                                    this.view.wrongPassword();
                                    exit(0);
                                }
                            }
                        }
                        break;
                case 3: exit(0);
                case 0: int op = this.view.wrongUser();
                        if (op == 1)
                        {
                            this.model.inserirProprietario(new Proprietario(this.view.inserirNovoUser(nif).toArray(String[]::new)));
                        }else if(op == 2){
                            List<String> dados = this.view.inserirNovoUser(nif);
                            String[] local = this.view.localizacao();

                            dados.add(local[0]);
                            dados.add(local[1]);

                            String[] teste = dados.toArray(String[]::new);

                            this.model.inserirCliente(new Cliente(teste));
                            this.model.saveStatus();
                        }
                        break;
            }
        }
    }

    private void proprietarioControl() {
        int op;
        do {
            op = this.view.proprietarioMenu();
            switch (op) {
                case 1: this.model.inserirVeiculo(new Veiculo(
                        this.view.getTipo(), this.view.getMarca(),
                        this.view.getMatricula(), this.nif,
                        this.view.getVelMedia(), this.view.getPrecoKm(),
                        this.view.getConsumoKm(), this.view.getAutonomia(),
                        this.view.getLocalizacao()));
                        this.model.saveStatus();
                        break;
                case 2: String matricula = this.view.getMatricula();
                        Veiculo v = this.model.getVeiculo(matricula, this.nif);
                        if(v != null) {
                            if (v.getDisponivel())
                                out.println("Esse veículo já se encontra disponível");
                            else {
                                v.setDisponivel();
                                this.model.inserirVeiculo(v);
                            }
                            this.model.saveStatus();
                        }
                        else {
                            this.view.veiculoNaoExistente(matricula);
                        }
                        break;
                case 3: abastecimento();
                        this.model.saveStatus();
                        break;
                case 4: String mat = this.view.getMatricula();
                        Veiculo vei = this.model.getVeiculo(mat, this.nif);
                        if(vei!=null) {
                            double precoKm = this.view.getPrecoKm();
                            vei.setPrecoKm(precoKm);
                            this.model.inserirVeiculo(vei);
                            this.model.saveStatus();
                        }
                        else this.view.veiculoNaoExistente(mat);
                        break;
                case 5: Map<String, List<Pedido>> lista = this.model.getProprietario(nif).getListaDeEspera();
                        if(!lista.isEmpty())
                            this.view.printClientesPendentes(lista);
                        else
                            this.view.naoHaPedidos();
                        break;
                case 6: User p = this.model.getUser(nif);
                        List<Aluguer> hist = p.getHistorico();
                        if(!hist.isEmpty()) {
                            Aluguer aluguer = hist.get(0);
                            out.println(aluguer.getPrecoViagem() + "€");
                            this.model.saveStatus();
                        }else this.view.naoHaPedidos();
                        break;
                case 7: String matr = this.view.getMatricula();
                        if(this.model.containsMatricula(matr)) {
                            Proprietario prop = this.model.getProprietario(nif);
                            prop.validarAluguer(matr, this.view.validarAluguer());
                            this.model.inserirProprietario(prop);
                        }else this.view.veiculoNaoExistente(matr);
                        break;
                case 8: this.view.getPerfil(this.model.getProprietario(nif));
                        break;
                case 9: String nifCheck = this.view.getNif();
                        Cliente c = this.model.getCliente(nifCheck);
                        if(c!=null)
                            this.view.getPerfil(c);
                        else
                            this.view.userNaoExiste(nifCheck);
                        break;
                case 0: break;
            }
        }while (op != 0) ;
    }

    private void abastecimento() {
        String matricula = view.getMatricula();
        Veiculo v = this.model.getVeiculo(matricula, this.nif);
        if (v != null) {
            int autonomia = v.getAutonomia();
            double consumokm = v.getConsumoKm();
            double total;
            double op = this.view.abastecimento(v.getTipo(), precoCombusivel(v.getTipo()), v.getAutonomia());
            if (op == 0) { //nao abastece
            } else if (op == 1) { //Atestar deposito
                total = ((1000 - autonomia) * consumokm) * precoCombusivel(v.getTipo());
                v.setAutonomia(1000);
                this.model.inserirVeiculoAbastecido(v);
                out.println("O veiculo ficou com uma autonomia de " + v.getAutonomia() + "Kms, e gastou " + total + "EUR");
            } else { //abastecer a quantidade pretendida
                autonomia += (int) (op / precoCombusivel(v.getTipo()) / consumokm);
                v.setAutonomia(autonomia);
                this.model.inserirVeiculoAbastecido(v);
                out.println("O veiculo ficou com uma autonomia de " + autonomia + "Kms");
            }
        }
        else view.veiculoNaoExistente(matricula);
    }

    private void clienteControl() {
        int op;
        do {
            op = this.view.clienteMenu();
            switch (op) {
                case 1: Veiculo maisProx = this.model.getVeiculoMaisProximo(nif);
                        if (maisProx != null) {
                            this.view.printVeiculo(maisProx,
                                    maisProx.getLocalizacao().distanceTo(
                                            this.model.getCliente(nif).getLocalizacao()
                                    ));
                        }else this.view.veiculoNaoExistente();
                        break;
                case 2: Veiculo maisBar = this.model.getVeiculoMaisBarato();
                        if(maisBar != null) {
                            this.view.printVeiculo(maisBar,
                                    maisBar.getLocalizacao().distanceTo(
                                            this.model.getCliente(nif).getLocalizacao()
                                    ));
                        }else this.view.veiculoNaoExistente();
                        break;
                case 3: double distancia = this.view.getDistancia();
                        Veiculo maisBaratDentroDist = this.model.getVeiculoMaisBarato(nif,distancia);
                        if(maisBaratDentroDist != null) {
                            this.view.printVeiculo(maisBaratDentroDist,
                                    maisBaratDentroDist.getLocalizacao().distanceTo(
                                            this.model.getCliente(nif).getLocalizacao()
                                    ));
                        }else this.view.veiculoNaoExistente();
                        break;
                case 4: String tipo = this.view.getTipo();
                        List<Veiculo> listaDeCarros = this.model.getListaVeiculos(tipo);
                        this.view.printListaVeiculos(listaDeCarros, this.model.getCliente(nif).getLocalizacao());
                        break;
                case 5: int aut = this.view.getAutonomia();
                        List<Veiculo> veiculosPossiveis = this.model.getListaVeiculos(aut);
                        this.view.printListaVeiculos(veiculosPossiveis, this.model.getCliente(nif).getLocalizacao());
                        break;
                case 6: String matricula = this.view.getMatricula();
                        if (this.model.containsMatricula(matricula)) {
                            Ponto<Double> destino = this.view.getDestino();
                            int queue = this.model.fazerPedido(nif, matricula, destino);
                            out.println("Está na posição " + queue + " na lista de espera");
                            this.model.saveStatus();
                        }else this.view.veiculoNaoExistente(matricula);
                        break;
                case 7: Pedido p = this.model.getPedido(nif);
                        if(p == null) this.view.naoHaPedidos();
                        else if(p.getEstado() == 2) {
                            this.view.pedidoRejeitado();
                            this.model.pedidoRejeitado(p);
                            this.model.saveStatus();
                        }else if(p.getEstado() == 1){
                            this.view.pedidoAceite();
                            this.model.pedidoAceite(p); //efetua aluguer
                            this.model.classifica(p.getVeiculo().getNifProp(), this.view.getAvaliacaoProprietario());
                            this.model.classifica(p.getVeiculo().getMatricula(), this.view.getAvaliacaoVeiculo());
                            this.model.saveStatus();
                        }else if(p.getEstado() == 0){
                            this.view.pedidoPendente();
                        }
                        break;
                case 8: this.view.getPerfil(this.model.getCliente(nif));
                        break;
                case 9: String nifCheck = this.view.getNif();
                        Proprietario pro = this.model.getProprietario(nifCheck);
                        if(pro != null)
                            this.view.getPerfil(pro);
                        else
                            this.view.userNaoExiste(nifCheck);
                        break;
                case 0: break;
            }
        }while (op != 0);
    }

    private double precoCombusivel(String str){
        final double preco = 1.559;
        double ret = 0;
        switch (str){
            case "Gasolina": ret = preco;
                             break;
            case "Eletrico": ret = 0;
                             break;
            case "Hibrido":  ret = preco/2.1;
                             break;
        }
        return ret;
    }

    public void saveStatus() {
    }
}
