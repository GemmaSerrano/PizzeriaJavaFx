package com.proyecto.modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Precios {

    private Map<String, Double> masa = new HashMap<>();
    private Map<String, Double> tipo = new HashMap<>();
    private Map<String, Double> ingredientes = new HashMap<>();
    private Map<String, Double> tamaño = new HashMap<>();
    private Double gratinado;
    private Double beber;
    

    public Precios() {
        masa.put("Normal", 3.);
        masa.put("Integral", 3.5);

        ingredientes.put("Sin extras", 0.0);
        ingredientes.put("Queso", 0.75);
        ingredientes.put("Tomate", 1.5);
        ingredientes.put("Cebolla", 0.75);
        ingredientes.put("jamón", 0.5);
        ingredientes.put("olivas", 1.);

        tipo.put("Barbacoa", 7.);
        tipo.put("Mexicana", 8.5);
        tipo.put("Básica", 7.0);
        tipo.put("Cuatro quesos", 5.0);
        

        tamaño.put("mediana", 15.);
        tamaño.put("familiar", 30.);
        
       beber = 2.0;
       gratinado = 0.02;

    }

    public Precios(
            Map<String, Double> masa,
            Map<String, Double> tipo,
            Map<String, Double> ingredientes,
            Map<String, Double> tamaño
                        
    ) {
        
        this.masa = masa;
        this.tipo = tipo;
        this.ingredientes = ingredientes;
        this.tamaño = tamaño;
    }

    public Double getGratinado() {
        return gratinado;
    }

    public void setGratinado(Double gratinado) {
        this.gratinado = gratinado;
    }

    public Double getBeber() {
        return beber;
    }

    public void setBeber(Double beber) {
        this.beber = beber;
    }

   
    
    public  Double precioDeMasa(String masa) {
        return this.masa.get(masa);
    }

    public Double precioDeTipo(String tipo) {
        return this.tipo.get(tipo);
    }

    public Double precioDeIngredientes(Set<String> ingredientesAsumar) {
        Double precioIngredientes = 0.00;

        for (String ingrediente : ingredientesAsumar) {
            precioIngredientes += ingredientes.get(ingrediente);
        }

        return precioIngredientes;
    }

    public Double porcentajeDeTamaño(String tamaño) {
        return this.tamaño.get(tamaño);
    }

       
    public Set<String> tiposMasa() {
        return masa.keySet();
    }

    public Set<String> tiposTiposPizza() {
        return tipo.keySet();
    }

    public Set<String> tiposIngrediente() {
        return ingredientes.keySet();
    }

    public Set<String> tiposTamaño() {
        return tamaño.keySet();
    }
}
