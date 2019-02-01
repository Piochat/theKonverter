/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koke.controlador;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

/**
 *
 * @author georg
 */
public class ControlArchivos {

    private List<File> listFiles;

    /*Genera una lista de archivos
     * obtenida del file chooser
     */
    public ArrayList<Image> getListFiles() {
        ArrayList<Image> lista = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccion de Imagenes");

        //Cambia el directorio dependiendo del sistema operativo
        String os = System.getProperty("os.name");
        if (!os.contains("Windows")) {
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home")));
        } // end if

        /* Configruacion del file chooser
		 * Filtros, Multiple seleccion
         */
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png"));
        listFiles = fileChooser.showOpenMultipleDialog(null);

        for (File file : listFiles) {
            Image image = new Image(file.toURI().toString());
            lista.add(image);
        } // end foreach

        return lista;
    } // end function

    /*
     * Genera una ArrayList de tipo File
     * para el control y conversion de 
     * los mismos
     */
    public ArrayList<File> getFiles() {
        ArrayList<File> list = new ArrayList<>(listFiles);

        return list;
    }

    /*
     * Método generado der un archivo .zip
     * que será sobrescriot con .cbz
     */
    public void convertZipCbz(ArrayList<File> files) {
        File archivo = saveFile();
        String rutaZip = archivo.getAbsolutePath().replaceAll(".cbz", ".zip");

        System.out.println(rutaZip);

        try {
            // create byte buffer
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(rutaZip);
            try (ZipOutputStream salidaZip = new ZipOutputStream(fos)) {
                for (File f : files) {
                    try (FileInputStream fis = new FileInputStream(f)) {
                        salidaZip.putNextEntry(new ZipEntry(f.getName()));

                        int length;

                        while ((length = fis.read(buffer)) > 0) {
                            salidaZip.write(buffer, 0, length);
                        }

                        salidaZip.closeEntry();
                    }
                }
            }
            File zip = new File(rutaZip);

            zip.renameTo(archivo);
        } catch (IOException e) {
            System.err.println(e.getMessage() + " " + e.toString());
        }
    }

    /*
     * Método generador del
     * Archivo pdf
     */
    public void convertPdf(ArrayList<File> files) {
        try {
            Document doc = new Document();
            FileOutputStream out = new FileOutputStream(saveFilePdf());
            PdfWriter.getInstance(doc, out);

            //Comprueba el tamaño real de la imagen para enviarlo a la pagina
            doc.setPageSize(imageSize(files.get(0)));
            doc.setMargins(0, 0, 0, 0);

            doc.open(); // Abrimos docuemnto Para realizar los cambios 

            for (File file : files) {
                // Convertivmos los File a Imagen de la libreria itext
                com.itextpdf.text.Image imagen = com.itextpdf.text.Image.getInstance(file.toString());
                imagen.setAlignment(Element.ALIGN_CENTER);

                doc.setPageSize(imageSize(file));
                doc.setMargins(0, 0, 0, 0);
                
                doc.newPage(); // invoca una pagina nueva en la cual se aplican los comabios
                doc.add(imagen); // Imprime la imagen en el PDF
            }
            
            doc.close();
        } catch (DocumentException | FileNotFoundException e) {
        } catch (IOException ex) {
            Logger.getLogger(ControlArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Retorna la ruta seleccionada donde será
     * grabado el archivo
     */
    private File saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccion ruta de Guardado");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Comics", "*.cbz", "*.cbr")
        );
        fileChooser.setInitialFileName("images.cbz");
        File file = fileChooser.showSaveDialog(null);
        return file;
    }

    /*
     * Retorna la ruta seleccionada donde será
     * grabado el archivo
     */
    private File saveFilePdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccion ruta de Guardado");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documentos", "*.pdf")
        );
        fileChooser.setInitialFileName("images.pdf");
        File file = fileChooser.showSaveDialog(null);
        return file;
    }

    public Rectangle imageSize(File f) {
        Rectangle r = null;
        com.itextpdf.text.Image imagen = null;

        try {
            imagen = com.itextpdf.text.Image.getInstance(f.toString());
            r = new Rectangle(imagen.getScaledWidth(), imagen.getScaledHeight());
        } catch (BadElementException | IOException e) {
            System.err.println(e.getMessage() + " " + e.toString());
        }
        return r;
    }
}
