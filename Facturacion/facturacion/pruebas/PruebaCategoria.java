package com.tuempresa.facturacion.pruebas;
 
import org.openxava.tests.*;
 
public class PruebaCategoria extends ModuleTestBase {
 
    public PruebaCategoria(String nombrePrueba) {
        super(nombrePrueba, "Facturacion", "Categoria");
    }
 
    public void testCategoriasEnLista() throws Exception {
        login("admin", "admin");
        assertValueInList(0, 0, "MÚSICA"); 
        assertValueInList(1, 0, "LIBROS"); 
        assertValueInList(2, 0, "SOFTWARE"); 
    }
 
}
