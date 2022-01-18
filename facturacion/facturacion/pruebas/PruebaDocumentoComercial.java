package com.tuempresa.facturacion.pruebas;
 
import static org.openxava.jpa.XPersistence.getManager;

import java.time.*;
import java.time.format.*;

import javax.persistence.*;

import org.openxava.tests.*;
 
abstract public class PruebaDocumentoComercial extends ModuleTestBase {
 
    private String numero; 
    private String modelo; 
 
    public PruebaDocumentoComercial(String nombrePrueba, String nombreModulo) {
        super(nombrePrueba, "Facturacion", nombreModulo);
        this.modelo = nombreModulo; 
    }
 
    public void testCrear() throws Exception {
        login("admin", "admin");
        calcularNumero(); 
        verificarValoresDefecto();
        escogerCliente();
        anyadirDetalles();
        ponerOtrasPropiedades();
        grabar();
        verificarBeneficioEstimado(); 
        verificarCreado();
        borrar();
    }
    
    public void testPapelera() throws Exception {
        login("admin", "admin");
        confirmarSoloUnaPaginaEnLista(); 
     
        // Borrar desde modo detalle
        int numeroFilasInicial = getListRowCount();
        String anyo1 = getValueInList(0, 0);
        String numero1 = getValueInList(0, 1);
        execute("List.viewDetail", "row=0");
        execute("Facturacion.delete");
        execute("Mode.list");
     
        assertListRowCount(numeroFilasInicial - 1); 
        confirmarDocumentoNoEstaEnLista(anyo1, numero1); 
     
        // Borrar desde el modo lista
        String anyo2 = getValueInList(0, 0);
        String numero2 = getValueInList(0, 1);
        checkRow(0);
        execute("Facturacion.deleteSelected");
        assertListRowCount(numeroFilasInicial - 2); 
        confirmarDocumentoNoEstaEnLista(anyo2, numero2); 
     
        // Verificar la entidades borradas en el módulo papelera
        changeModule("Papelera" + modelo); 
        confirmarSoloUnaPaginaEnLista();
        int numeroFilasInicialPapelera = getListRowCount();
        confirmarDocumentoEstaEnLista(anyo1, numero1); 
        confirmarDocumentoEstaEnLista(anyo2, numero2); 
     
        // Restaurar usando una acción de fila
        int fila1 = getFilaDocumentoEnLista(anyo1, numero1);
        execute("Papelera.restaurar", "row=" + fila1);
        assertListRowCount(numeroFilasInicialPapelera - 1); 
        confirmarDocumentoNoEstaEnLista(anyo1, numero1); 
     
        // Restaurar seleccionando una fila y usando el botón de abajo
        int fila2 = getFilaDocumentoEnLista(anyo2, numero2);
        checkRow(fila2);
        execute("Papelera.restaurar");
        assertListRowCount(numeroFilasInicialPapelera - 2); 
        confirmarDocumentoNoEstaEnLista(anyo2, numero2); 
     
        // Verificar las entidades restauradas
        changeModule(modelo);
        assertListRowCount(numeroFilasInicial); 
        confirmarDocumentoEstaEnLista(anyo1, numero1); 
        confirmarDocumentoEstaEnLista(anyo2, numero2);
    }

    private void confirmarSoloUnaPaginaEnLista() throws Exception {
        assertListNotEmpty(); // De ModuleTestBase
        assertTrue("Debe tener menos de 10 filas para ejecutar esta prueba",
            getListRowCount() < 10);
    }
    
    private void confirmarDocumentoNoEstaEnLista(String anyo, String numero)
	    throws Exception
	{
	    assertTrue(
	        "Documento " + anyo + "/" + numero + " no debe estar en la lista",
	            getFilaDocumentoEnLista(anyo, numero) < 0);
	}
	 
	private void confirmarDocumentoEstaEnLista(String anyo, String numero)
	    throws Exception
	{
	    assertTrue(
	        "Documento " + anyo + "/" + numero + " debe estar en la lista",
	            getFilaDocumentoEnLista(anyo, numero) >= 0);
	}
	 
	private int getFilaDocumentoEnLista(String anyo, String numero)
	    throws Exception
	{
	    int c = getListRowCount();
	    for (int i = 0; i < c; i++) {
	        if (anyo.equals(getValueInList(i, 0)) &&
	            numero.equals(getValueInList(i, 1)))
	        {
	            return i;
	        }
	    }
	    return -1;
	}

    private void verificarValoresDefecto() throws Exception {
        execute("CRUD.new");
        assertValue("anyo", getAnyoActual());
        assertValue("numero", ""); 
        assertValue("fecha", getFechaActual());
    }
 
    private void escogerCliente() throws Exception {
        setValue("cliente.numero", "1");
        assertValue("cliente.nombre", "JAVIER PANIZA"); 
    }
 
    private void anyadirDetalles() throws Exception {
        assertCollectionRowCount("detalles", 0);
    	
        // Antes de ejecutar esta prueba asegurate de que
        //   producto 1 tenga 19 como precio y 
        //   producto 2 tenga 20 como precio
     
        // Añadir una línea de detalle
        setValueInCollection("detalles", 0, "producto.numero", "1");
        assertValueInCollection("detalles", 0,
            "producto.descripcion", "Peopleware: Productive Projects and Teams");
        assertValueInCollection("detalles", 0,
            "precioPorUnidad", "19,00"); 
        setValueInCollection("detalles", 0, "cantidad", "2");
        assertValueInCollection("detalles", 0,
            "importe", "38,00"); 
     
        // Verificando propiedades de total de la colección
        assertTotalInCollection("detalles", 0, "importe", "38,00"); 
        assertTotalInCollection("detalles", 1, "importe", "21"); 
        assertTotalInCollection("detalles", 2, "importe", "7,98"); 
        assertTotalInCollection("detalles", 3, "importe", "45,98"); 
     
        // Añadir otro detalle
        setValueInCollection("detalles", 1, "producto.numero", "2");
        assertValueInCollection("detalles", 1, "producto.descripcion", "Arco iris de lágrimas");
        assertValueInCollection("detalles", 1, "precioPorUnidad", "20,00"); 
        setValueInCollection("detalles", 1, "precioPorUnidad", "10,00"); 
        setValueInCollection("detalles", 1, "cantidad", "1");
        assertValueInCollection("detalles", 1, "importe", "10,00");
     
        assertCollectionRowCount("detalles", 2); 
     
        // Verificando propiedades de total de la colección
        assertTotalInCollection("detalles", 0, "importe", "48,00");
        assertTotalInCollection("detalles", 1, "importe", "21");
        assertTotalInCollection("detalles", 2, "importe", "10,08");
        assertTotalInCollection("detalles", 3, "importe", "58,08");
    }
    
    private void verificarBeneficioEstimado() throws Exception {
        execute("Mode.list"); 
        setConditionValues(new String [] { 
            getAnyoActual(), getNumero() 
        });
        execute("List.filter"); 
        assertValueInList(0, 0, getAnyoActual()); 
        assertValueInList(0, 1, getNumero()); 
        assertValueInList(0, "beneficioEstimado", "5,81"); 
        execute("List.viewDetail", "row=0"); 
    }
 
    private void ponerOtrasPropiedades() throws Exception {
        setValue("observaciones", "Esto es una prueba JUNIT");
    }
 
    private void grabar() throws Exception {
        execute("CRUD.save");
        assertNoErrors();
        assertValue("cliente.numero", "");
        assertCollectionRowCount("detalles", 0);
        assertValue("observaciones", "");
    }
 
    private void verificarCreado() throws Exception {
        assertValue("anyo", getAnyoActual());
        assertValue("numero", getNumero());
        assertValue("fecha", getFechaActual());
        assertValue("cliente.numero", "1");
        assertValue("cliente.nombre", "JAVIER PANIZA");
        assertCollectionRowCount("detalles", 2);
     
        // Fila 0
        assertValueInCollection("detalles", 0, "producto.numero", "1");
        assertValueInCollection("detalles", 0, "producto.descripcion",
            "Peopleware: Productive Projects and Teams");
        assertValueInCollection("detalles", 0, "cantidad", "2");
     
        // Fila 1
        assertValueInCollection("detalles", 1, "producto.numero", "2");
        assertValueInCollection("detalles", 1, "producto.descripcion",
            "Arco iris de lágrimas");
        assertValueInCollection("detalles", 1, "cantidad", "1");
        assertValue("observaciones", "Esto es una prueba JUNIT");
    }
 
    private void borrar() throws Exception {
        execute("Facturacion.delete");
        assertNoErrors();
    }
 
    private String getAnyoActual() { 
        return Integer.toString(LocalDate.now().getYear()); 
    }
     
    private String getFechaActual() { 
        return LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
     
    private void calcularNumero() {
        Query query = getManager().createQuery(
            "select max(f.numero) from " +
            modelo + 
            " f where f.anyo = :anyo");
        query.setParameter("anyo", LocalDate.now().getYear());
        Integer ultimoNumero = (Integer) query.getSingleResult();
        if (ultimoNumero == null) ultimoNumero = 0;
        numero = Integer.toString(ultimoNumero + 1);
    }
     
    private String getNumero() {
        return numero;
    }
 
}

