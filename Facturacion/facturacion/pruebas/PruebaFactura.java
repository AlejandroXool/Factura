package com.tuempresa.facturacion.pruebas;
 
public class PruebaFactura extends PruebaDocumentoComercial {
 
    public PruebaFactura(String nombrePrueba) {
        super(nombrePrueba, "Factura");
    }
    
    public void testAnyadirPedidos() throws Exception {
        login("admin", "admin");
        assertListNotEmpty();
        execute("List.orderBy", "property=numero");
        execute("List.viewDetail", "row=0");
        execute("Sections.change", "activeSection=1");
        assertCollectionRowCount("pedidos", 0);
        execute("Collection.add", "viewObject=xava_view_section1_pedidos");

        seleccionarPrimerPedidoConEntregadoIgual("Entregado"); 
        seleccionarPrimerPedidoConEntregadoIgual(""); 
        execute("AddToCollection.add"); 
        assertError("¡ERROR! 1 elemento(s) NO añadido(s) a Pedidos de Factura");
        assertMessage("1 elemento(s) añadido(s) a Pedidos de Factura");
     
        assertCollectionRowCount("pedidos", 1);
        checkRowCollection("pedidos", 0);
        execute("Collection.removeSelected", "viewObject=xava_view_section1_pedidos");
        assertCollectionRowCount("pedidos", 0);
    }
    
    private void seleccionarPrimerPedidoConEntregadoIgual(String valor) throws Exception {
        int c = getListRowCount(); 
        for (int i = 0; i < c; i++) {
            if (valor.equals(getValueInList(i, 12))) {     
                checkRow(i);
                return;
            }
        }
        fail("Debe tener al menos una fila con entregado=" + valor);
    }
 
}
