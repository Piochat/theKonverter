package com.koke.thekonverter;

import com.koke.controlador.ControlArchivos;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class FXMLController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private ListView listView;

    @FXML
    private Button btnUp;

    @FXML
    private void open(ActionEvent event) {
        try {
            //Llenado de la lista de Imagenes, la lista de Archivos
            //Y la observable list
            listaImagenes = archivos.getListFiles();
            listaArchivos = archivos.getFiles();
            for (Image image : listaImagenes) {
                datos.add(image);
            } // end foreach

            // Cell factory para el llenada de listView con Imagenes
            listView.setCellFactory(new Callback<ListView<Image>, ListCell<Image>>() {

                @Override
                public ListCell<Image> call(ListView<Image> param) {
                    ListCell<Image> cell = new ListCell<Image>() {

                        @Override
                        protected void updateItem(Image i, boolean b) {
                            super.updateItem(i, b);

                            if (i != null) {
                                ImageView imageView = new ImageView(i);
                                imageView.setFitWidth(i.getWidth() / 20);
                                imageView.setFitHeight(i.getHeight() / 20);
                                setGraphic(imageView);
                            } else {
                                setGraphic(null);
                            } // end if

                        } // end updateItem

                    }; // end cell
                    return cell;
                }// end call
            });

            imageView.setImage(listaImagenes.get(0));
            listView.setItems(datos);
        } catch (Exception e) {
            System.err.println(e.getMessage() + " " + e.toString());
        }
    } // end open

    @FXML // Limpia todas las listas
    private void clear(ActionEvent event) {
        try {
            listaArchivos.clear();
            listaImagenes.clear();
            datos.clear();
            listView.getItems().clear();
            imageView.setImage(null);
        } catch (Exception e) {
            System.err.println(e.getMessage() + " Acción Inválida");
        }
    } // end clear

    @FXML // Envia el item seleccionado al imageView
    private void imagePicked() {
        Image v = (Image) listView.getSelectionModel().getSelectedItem();
        imageView.setImage(v);
    } // en imagePicked

    @FXML // Elimina el item selecionado
    private void delete(ActionEvent event) {
        try {
            Image v = (Image) listView.getSelectionModel().getSelectedItem();
            listaArchivos.remove(listView.getSelectionModel().getSelectedIndex());
            listaImagenes.remove(v);
            datos.remove(v);
            listView.getItems().remove(v);
            imageView.setImage(null);
        } catch (Exception e) {
            System.err.println(e.getMessage() + " Acción Inválida");
        }
    } // end delete

    @FXML // Sube o Baja el item seleccionado
    private void upDown(ActionEvent event) {
        if (event.getSource() == btnUp) {

            /*
             * Cambia la posicion del item seleccionado por medio del
             * indice optenido de la listView
             */
            int indice = listView.getSelectionModel().getSelectedIndex();

            if (indice > 0) {
                try {
                    Image v = (Image) listView.getSelectionModel().getSelectedItem();

                    File c = listaArchivos.get(indice);
                    listaArchivos.remove(listView.getSelectionModel().getSelectedIndex());
                    listaArchivos.add(indice - 1, c);

                    listaImagenes.remove(v);
                    listaImagenes.add(indice - 1, v);

                    datos.remove(v);
                    datos.add(indice - 1, v);

                    listView.getItems().remove(v);
                    listView.getItems().add(indice - 1, v);
                    listView.scrollTo(v);
                    listView.getSelectionModel().select(v);
                } catch (Exception e) {
                    System.err.println("Acción inválida");
                }

            } //end if 

        } else {

            /*
             * Cambia la posicion del item seleccionado por medio del
             * indice optenido de la listView
             */
            int indice = listView.getSelectionModel().getSelectedIndex();

            if (indice < (listView.getItems().size() - 1)) {
                try {
                    Image v = (Image) listView.getSelectionModel().getSelectedItem();

                    File c = listaArchivos.get(indice);
                    listaArchivos.remove(listView.getSelectionModel().getSelectedIndex());
                    listaArchivos.add(indice + 1, c);

                    listaImagenes.remove(v);
                    listaImagenes.add(indice + 1, v);

                    datos.remove(v);
                    datos.add(indice + 1, v);

                    listView.getItems().remove(v);
                    listView.getItems().add(indice + 1, v);
                    listView.scrollTo(v);
                    listView.getSelectionModel().select(v);
                } catch (Exception e) {
                    System.err.println("Acción inválida");
                }

            } // end if

        } // end if
    } // end upDown

    @FXML
    private void convertCbz(ActionEvent event) {
        try {

            archivos.convertZipCbz(listaArchivos);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("The Konverter");
            alert.setHeaderText("Exportacion Exitosa");
            alert.setContentText("Archivo CBZ creado");
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Fallo en la lista");
            System.err.println(listaArchivos.size());
            System.err.println(e.getMessage() + " Barry Allen es Flash");
        }
    } // end convert

    @FXML
    private void convertPdf(ActionEvent event) {
        try {
            archivos.convertPdf(listaArchivos);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("The Konverter");
            alert.setHeaderText("Exportacion Exitosa");
            alert.setContentText("Archivo PDF creado");
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Fallo en la lista");
            System.err.println(listaArchivos.size());
            System.err.println(e.getMessage() + " Barry Allen es Flash");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private final ControlArchivos archivos = new ControlArchivos();
    private ArrayList<Image> listaImagenes;
    private ObservableList<Image> datos = FXCollections.observableArrayList();
    private ArrayList<File> listaArchivos = new ArrayList<>();
}
