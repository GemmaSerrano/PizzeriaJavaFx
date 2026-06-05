package com.proyecto;

import com.proyecto.modelo.Pizza;
import com.proyecto.modelo.Precios;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class PrimaryController implements Initializable {

    private Pizza pizza;
    private Precios precios;
    ObservableList<Pizza> pizzasElegidas = FXCollections.observableArrayList();
    LocalDateTime fecha = LocalDateTime.now();

    @FXML
    private RadioButton radioNormal;
    @FXML
    private ToggleGroup grupoRadiosMasa;
    @FXML
    private RadioButton radioIntegral;
    @FXML
    private Label labelMasa;
    @FXML
    private Spinner<String> spinnerTamaño;
    @FXML
    private ComboBox<String> choiceTipo;
    @FXML
    private Label labelTipo;
    @FXML
    private Label labelIngredientes;
    @FXML
    private Label labelTamaño;
    @FXML
    private ListView<String> listViewIngredientes;
    @FXML
    private Label labelConsejoIngredientes;
    @FXML
    private TextArea textareaPedido;
    @FXML
    private Rectangle rectanglePanelUsuario;
    @FXML
    private ListView<Pizza> listViewPizzasElegidas;
    @FXML
    private Button btRegistrar;
    @FXML
    private CheckBox chkBebida;
    @FXML
    private CheckBox chkGratinar;
    @FXML
    private Label lbPizza;
    @FXML
    private Label lbPrecio;
    @FXML
    private Button btNuevoPedido;
    @FXML
    private Label labelMasa1;
    @FXML
    private Button btImprimirTicket;
    @FXML
    private Button btResetPizza;
    @FXML
    private ImageView imgReset;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crearPizzaValoresDefecto();
        calcularPrecioPedido();

    }

    public void crearPizzaValoresDefecto() {

        //CREAMOS LOS OBJETOS PIZZA Y PRECIOS
        pizza = new Pizza();
        precios = new Precios();
        //LE PASAMOS LA REFERENCIA DE LOS PRECIOS A LA PIZZA
        pizza.setPrecios(precios);

        //CARGAMOS CON LOS TIPOS DE PIZZA EL CHOICE
        choiceTipo.setItems(FXCollections.observableArrayList(precios.tiposTiposPizza()));

        //CARGAMOS LOS COMPONENTES CON LOS VALORES OBTENIDOS DE LA CLAS PRECIO
        listViewIngredientes.setItems(FXCollections.observableArrayList(precios.tiposIngrediente()));
        listViewIngredientes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); //PERMITE SELECCIONAR VARIOS ELEMENTOS CON TECLA CTRL
        listViewIngredientes.getSelectionModel().clearSelection();

        SpinnerValueFactory.ListSpinnerValueFactory<String> factoryTamaños = new SpinnerValueFactory.ListSpinnerValueFactory(FXCollections.observableArrayList(precios.tiposTamaño()));
        spinnerTamaño.setValueFactory(factoryTamaños);

        // valores por defecto 
        radioNormal.setSelected(true);
        choiceTipo.setValue("Barbacoa");
        chkBebida.setSelected(false);
        chkGratinar.setSelected(false);

        actualizarAutomatico();

    }

    private void masa() {
        //SE OBTIENE EL VALOR SELECCIONADO EN EL RADIO BUTTON DE MASA
        String masa = ((RadioButton) grupoRadiosMasa.getSelectedToggle()).getText();
        //SE PASA LA MASA SELECCIONADA A LA PIZZA
        pizza.setMasa(masa);
    }

    public void tamaño() {
        String tamaño = spinnerTamaño.getValue();
        pizza.setTamaño(tamaño);
    }

    private void ingredientes() {
        Set<String> ingredientesExtra = new HashSet<>();

        //LOS INGREDIENTES SELECCIONADOS LOS ALMACENAMOS EN UN SET
        for (String ingrediente : listViewIngredientes.getSelectionModel().getSelectedItems()) {
            if (ingrediente != null) {
                ingredientesExtra.add(ingrediente);
            }
        }
        //ALMACENAMOS LOS INGREDIENTES EN LA PIZZA
        pizza.setIngredientesExtra(ingredientesExtra);
    }

    private void tipo() {
        String tipo = choiceTipo.getValue();
        pizza.setTipo(tipo);
    }

    @FXML
    private void actualizarAutomatico() {
        lbPizza.setText(String.format("PIZZA %s", pizza.getIdPizza()));

        masa();
        tipo();
        tamaño();
        ingredientes();

        textareaPedido.setText(pizza.composicion());

    }

    //Deja los valores y precio de inicio, excepto el Id, que no cambia
    //Sólo para rectificar una pizza antes de registrarla
    @FXML
    private void resetPizza(MouseEvent event) {

        listViewIngredientes.getSelectionModel().clearSelection();
        spinnerTamaño.getValueFactory().setValue("mediana");
        radioNormal.setSelected(true);
        choiceTipo.setValue("Barbacoa");
        chkBebida.setSelected(false);
        chkGratinar.setSelected(false);
        pizza.setBebida(false);
        pizza.setGratinar(false);
        actualizarAutomatico();
        alertasInformacion("Elige las opciones de tu pizza","Aceptar para continuar tu pedido");
    }

    public Double calcularPrecioPedido() {
        Double precioTotal = 0.0;

        for (Pizza pizza : pizzasElegidas) {

            listViewPizzasElegidas.setItems(pizzasElegidas);
            precioTotal += pizza.calcularPrecio();
        }
        lbPrecio.setText(String.format("PRECIO TOTAL %.2f€", precioTotal));
        return precioTotal;

    }

    @FXML
    private void registrarPizza(ActionEvent event) {
        masa();
        tipo();
        tamaño();
        ingredientes();

        pizzasElegidas.add(pizza);
        lbPrecio.setText(String.format("PRECIO TOTAL %.2f€", calcularPrecioPedido()));
        textareaPedido.setText(pizza.composicion());

        listViewPizzasElegidas.setItems(FXCollections.observableArrayList(pizzasElegidas));
        crearPizzaValoresDefecto();
        alertasInformacion("Pizza registrada","");
    }

    @FXML
    private void sumarBebida(ActionEvent event) {
        if (chkBebida.isSelected()) {
            pizza.setBebida(true);

        }
    }

    @FXML
    private void sumarGratinar(ActionEvent event) {
        if (chkGratinar.isSelected()) {
            pizza.setGratinar(true);
        }
    }

    
    @FXML
    private void reiniciarPedido(ActionEvent event) {
        pizza.setContadorPizzas(1);
        pizza.setIdPizza(1);
        pizzasElegidas.clear();
        listViewPizzasElegidas.setItems(pizzasElegidas);
        if (pizzasElegidas.isEmpty()) {
            lbPrecio.setText("TOTAL 0 €");
        } else {
            lbPrecio.setText(String.format("PRECIO TOTAL %.2f€", pizza.calcularPrecio()));
        }
        crearPizzaValoresDefecto();
    }

    @FXML
    private void eliminarPizza(MouseEvent event) {
        if (event.getClickCount() == 2) {
            mostrarAlertaConfirmaEliminar();
        }
        calcularPrecioPedido();
    }

    private void mostrarAlertaConfirmaEliminar() {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        int numeroPizza = listViewPizzasElegidas.getSelectionModel().getSelectedIndex() + 1;

        alerta.setTitle("Confirmación");
        alerta.setHeaderText("Confirma el borrado de pizza");
        alerta.setContentText("¿Estás seguro de que quieres eliminar la pizza " + numeroPizza + "?");

        ButtonType SI = new ButtonType("SI");
        ButtonType NO = new ButtonType("NO");
        alerta.getButtonTypes().setAll(SI, NO);

        Optional<ButtonType> respuesta = alerta.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == SI) {
            pizzasElegidas.remove(numeroPizza - 1);
            listViewPizzasElegidas.setItems(pizzasElegidas);
            alertasInformacion("La pizza " + numeroPizza + " ha sido eliminada",
                    "Pulsa Aceptar para continuar con el pedido");
        } else {
            alertasInformacion("HAS PULSADO NO BORRAR", "La pizza no se ha eliminado");
        }
    }

    private void alertasInformacion(String encabezado, String mensaje) {
        Alert alertasInf = new Alert(Alert.AlertType.INFORMATION);
        alertasInf.setTitle("Informacion: Pizzeria Gemma");
        alertasInf.setHeaderText(encabezado);
        alertasInf.setContentText(mensaje);
        alertasInf.showAndWait();
    }
    
    @FXML
    private void imprimirTicket(ActionEvent event) {
        int numTicket = 0;
        String nombreTicket = "";
        Path ticket = Paths.get(nombreTicket);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String fechaTicket = fecha.format(formato);
        String nota="";

        while (Files.exists(ticket)) {
            numTicket++;
            nombreTicket = "Ticket " + numTicket + ".txt";
            ticket = Paths.get(nombreTicket);
        }
        List<String> ticketPizzas = new ArrayList<>();

        for (Pizza pizzas : pizzasElegidas) {
            ticketPizzas.add("Pizza " + pizzas.getIdPizza() + "\n"
                    + pizzas.composicion() + "\n");
        }
        nota = "          Pizzería Gemma \n";
        nota += "         Tel. 657 84 76 39\n";
        nota += "       gemma@pizzeriaGemma.com\n";
        nota += " __________________________________\n";
        nota += "               DESGLOSE\n\n";
        nota += "TICKET N º :  "+ numTicket+"\n";
        nota += "FECHA: " + fechaTicket + "\n\n";
        
        for (String pizza : ticketPizzas) {
            nota += "      ___________________ \n  ";
            nota += pizza+"\n";
        }
        nota += "\n _______________________ \n  ";
        nota += lbPrecio.getText() + "\n\n";
        nota += " ____________________________________ \n\n";
        nota += "    GRACIAS POR SU VISITA!\n";

        try ( BufferedWriter linea = Files.newBufferedWriter(ticket,
                Charset.defaultCharset(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            
            linea.write(nota);
            alertasInformacion("TICKET CREADO CON EXITO", "REVISA EN EL DIRECTORIO DE ESTE PROYECTO");

        } catch (IOException e) {
            alertasInformacion("TICKET NO CREADO", "REVISA PEDIDO");
        }
        reiniciarPedido(new ActionEvent());
    }


}

