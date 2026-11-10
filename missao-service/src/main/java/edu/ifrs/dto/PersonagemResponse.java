package edu.ifrs.dto;

import edu.ifrs.model.Personagem;

public class PersonagemResponse { //após pesquisa: DTO (Data Transfer Object) é um objeto criado especificamente para transportar dados entre partes de um sistema.
                                  //no caso, eu gostaria de retornar o Boolean + os dados do Personagem
    private Boolean isAlive;
    private Personagem personagem;

    public Boolean getIsAlive() {
        return isAlive;
    }
    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }
    public Personagem getPersonagem() {
        return personagem;
    }
    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    } 
}
