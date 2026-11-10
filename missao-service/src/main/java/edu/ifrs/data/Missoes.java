package edu.ifrs.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import edu.ifrs.model.Missao;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped //para durar durante toda a execução do programa
public class Missoes {
    private ArrayList<Missao> missoes = new ArrayList<>();

    //gerador de ID único sequencial (começa no 1)
    private AtomicLong geradorId = new AtomicLong(1);

    public Missao cadastrarMissao(Missao novaMissao) {
        //gera o próximo ID único
        Long idGerado = geradorId.getAndIncrement();

        //grava ID gerado no objeto
        novaMissao.setId(idGerado);
        //garante que não cria missão diretamente com tripulantes
        novaMissao.setTripulacao(null);
        //adiciona objeto à lista
        missoes.add(novaMissao);

        return novaMissao;
    }

    public List<Missao> listarTodas() {
            return missoes;
    }

    public Missao consultarMissao(Long id) {
        for (Missao missao : missoes) {
            if (missao.getId().equals(id))
                return missao; //encontrou
        }
        return null; //não encontrou
    }

    public Boolean removerMissao(Long id) {
        Missao missao = consultarMissao(id);
        if (missao==null) { //não encontrada
            return false;
        } else {
            this.missoes.remove(missao); //encontrou, remove
            return true;
        }
    }
}
