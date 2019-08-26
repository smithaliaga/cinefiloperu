package com.pe.cinefilos.object.entities;

import java.io.Serializable;
import java.util.List;

public class Trivia extends EntityWSBase implements Serializable {

    public Long codigoTrivia;
    public List<TriviaPregunta> preguntas;

}
