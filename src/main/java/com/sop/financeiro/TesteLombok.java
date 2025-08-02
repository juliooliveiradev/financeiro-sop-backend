package com.sop.financeiro;

import com.sop.financeiro.model.Despesa;

public class TesteLombok {
    public static void main(String[] args) {
        Despesa d = new Despesa();
        d.setProtocolo("123"); // <-- se não compilar, Lombok não está ativo
    }
}

