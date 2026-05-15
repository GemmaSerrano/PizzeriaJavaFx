package com.proyecto.modelo;

import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;

public class Pizza {

    private String masa;
    private String tipo;
    private Set<String> ingredientesExtra;
    private String tamaño;
    private int idPizza;
    public static int contadorPizzas = 1;
    private boolean bebida;
    private boolean gratinar;

    private Precios precios;

    public Pizza() {
        idPizza = contadorPizzas;
        contadorPizzas++;
    }

    public Pizza(String masa, String tipo, Set<String> ingredientesExtra, String tamaño) {
        this.masa = masa;
        this.tipo = tipo;
        this.ingredientesExtra = ingredientesExtra;
        this.tamaño = tamaño;

    }

    public int getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }

    public static int getContadorPizzas() {
        return contadorPizzas;
    }

    public static void setContadorPizzas(int contadorPizzas) {
        Pizza.contadorPizzas = contadorPizzas;
    }

    public String getMasa() {
        return masa;
    }

    public void setMasa(String masa) {
        this.masa = masa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Set<String> getIngredientesExtra() {
        return ingredientesExtra;
    }

    public void setIngredientesExtra(Set<String> ingredientesExtra) {
        this.ingredientesExtra = ingredientesExtra;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public Precios getPrecios() {
        return precios;
    }

    public void setPrecios(Precios precios) {
        this.precios = precios;
    }

    public void setBebida(boolean bebida) {
        this.bebida = bebida;
    }

    public boolean getBebida() {
        return bebida;
    }

    public boolean getGratinar() {
        return gratinar;
    }

    public void setGratinar(boolean gratinar) {
        this.gratinar = gratinar;
    }

    public Double calcularPrecio() {
        Double precioTotal = 0.;

        Double precioMasa = precios.precioDeMasa(this.masa);
        Double precioTipo = precios.precioDeTipo(this.tipo);
        Double porcentajeTamaño = precios.porcentajeDeTamaño(this.tamaño);
        Double precioIngredientes = precios.precioDeIngredientes(this.ingredientesExtra);
        Double precioGratinar = precios.getGratinado();
        Double precioBebida = precios.getBeber();

        precioTotal = (precioMasa + precioTipo + precioIngredientes);
        precioTotal += (precioTotal * (porcentajeTamaño / 100));

        if (gratinar == true) {
            precioTotal += precioTotal * precioGratinar;
        }

        if (bebida == true) {
            precioTotal += precioBebida;
        }

        return precioTotal;
    }


    public String composicion() {
        String cadena = "";

        Double precioMasa = precios.precioDeMasa(this.masa);
        Double precioTipo = precios.precioDeTipo(this.tipo);
        Double porcentajeTamaño = precios.porcentajeDeTamaño(this.tamaño);
        Double precioIngredientes = precios.precioDeIngredientes(this.ingredientesExtra);

        cadena += "MASA: " + this.masa + " - " + precioMasa + "€";
        cadena += "\n";
        cadena += "TIPO: " + this.tipo + " - " + precioTipo + "€";
        cadena += "\n";
        cadena += "INGREDIENTES EXTRA: " + this.ingredientesExtra + " - " + precioIngredientes + "€";
        cadena += "\n";
        cadena += "TAMAÑO: " + this.tamaño + " - " + porcentajeTamaño + "%";
        cadena += "\n";

               
        if (gratinar == true) {
            cadena += "GRATINAR: 2%";
            cadena += "\n";
        
        }
        

        if (bebida == true) {
            cadena += "BEBIDA: 2 €";
            cadena += "\n";
        }

        cadena += String.format("TOTAL %.2f€", calcularPrecio());

        return cadena;
    }


    @Override
    public String toString() {

        return (String.format("Pizza %d %s %s %.2f€", idPizza, tipo, masa, calcularPrecio()));
    }

}
