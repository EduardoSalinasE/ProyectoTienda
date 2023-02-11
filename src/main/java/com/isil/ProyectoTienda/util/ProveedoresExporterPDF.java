package com.isil.ProyectoTienda.util;

import com.isil.ProyectoTienda.model.ReportesProductos;
import com.isil.ProyectoTienda.model.ReportesProveedores;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ProveedoresExporterPDF {

    private List<ReportesProveedores> listaReportes;

    public ProveedoresExporterPDF(List<ReportesProveedores> listaReportes){
        super();
        this.listaReportes = listaReportes;
    }

    private void escribirTabla(PdfPTable pdfPTable){
        PdfPCell celda = new PdfPCell();

        celda.setBackgroundColor(Color.blue);
        celda.setPadding(6);

        celda.setPhrase(new Phrase("ID"));
        pdfPTable.addCell(celda);

        celda.setPhrase(new Phrase("Nombre"));
        pdfPTable.addCell(celda);

        celda.setPhrase(new Phrase("ruc"));
        pdfPTable.addCell(celda);

        celda.setPhrase(new Phrase("Fecha"));
        pdfPTable.addCell(celda);

        celda.setPhrase(new Phrase("Tipo"));
        pdfPTable.addCell(celda);

        celda.setPhrase(new Phrase("Usuario"));
        pdfPTable.addCell(celda);
    }

    private void escribirDatos(PdfPTable pdfPTable){
        for (ReportesProveedores reportes : listaReportes){
            pdfPTable.addCell(String.valueOf(reportes.getId()));
            pdfPTable.addCell(reportes.getNombreProveedor());
            pdfPTable.addCell(reportes.getRuc());
            pdfPTable.addCell(String.valueOf(reportes.getFechaCreacion()));
            pdfPTable.addCell(reportes.getTipoOperacion());
            pdfPTable.addCell(String.valueOf(reportes.getUsuario()));
        }
    }

    public void exportar(HttpServletResponse response) throws IOException {
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();

        Paragraph titulo = new Paragraph("Reporte de Proveedores");
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(titulo);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[]{1f,2.3f,2f,6f,2f,4f});
        table.setWidthPercentage(110);

        escribirTabla(table);
        escribirDatos(table);

        documento.add(table);
        documento.close();
    }
}
