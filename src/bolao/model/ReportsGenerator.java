/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolao.model;

import bolao.ConnectionFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thayn
 */
public abstract class ReportsGenerator {

    public static void betsReport(int game_ID){
        Document d = new Document();
        Rectangle rect = new Rectangle(PageSize.A4.rotate());
        d.setPageSize(rect);
        PdfPTable table = null;
        ResultSetMetaData md;
        int columnCount;

        String sql = "SELECT Player.name, Bet.numbers "
                + "FROM Bet "
                + "INNER JOIN Player ON Player.player_ID = Bet.player_ID "
                + "WHERE Bet.game_ID = (?)"
                + "ORDER BY Player.name";
        Connection conn;
        PreparedStatement ps;

        try {
            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            ResultSet rs = ps.executeQuery();

            md = rs.getMetaData();
            columnCount = md.getColumnCount();
            table = new PdfPTable(4);
            PdfPCell cell = new PdfPCell(new Paragraph("Jogo " + game_ID));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            table.addCell("Jogador");
            table.addCell("Aposta");
            table.addCell("Jogador");
            table.addCell("Aposta");


            while (rs.next()){
                table.addCell("" + rs.getString(1));
                table.addCell("" + rs.getString(2));
                if (rs.next()){
                    table.addCell("" + rs.getString(1));
                    table.addCell("" + rs.getString(2));
                } else {
                    table.addCell("");
                    table.addCell("");
                }

            }
            conn.close();
            LocalDate ld = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddLLLLyyyy");
            PdfWriter.getInstance(d, new FileOutputStream("./PDF/" + ld.format(formatter) + "Apostas.pdf"));
            d.open();
            d.add(table);


        } catch (SQLException | DocumentException | FileNotFoundException ex) {
            Logger.getLogger(ReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        d.close();
    }

    public static void hitsPerDrawReport(int game_ID, int draw_ID, int ordr){
        Document d = new Document();
        Rectangle rect = new Rectangle(PageSize.A4.rotate());
        d.setPageSize(rect);
        PdfPTable table;
        ResultSetMetaData md;
        int columnCount;
        Connection conn;
        PreparedStatement ps;


        String sql;


        if (ordr == 0) {
            sql = "SELECT Player.name, Bet.numbers, bd.hitNumbers, Bet.remaining, bd.hitSize, Bet.remainingSize "
                    + "FROM Bet "
                    + "INNER JOIN Player ON Player.player_ID = Bet.player_ID "
                    + "INNER JOIN Bet_Draw as bd ON bd.bet_ID = Bet.bet_ID AND bd.draw_ID = ?"
                    + "WHERE Bet.game_ID = (?) "
                    + "ORDER BY Bet.remainingSize DESC";
        } else {
            sql = "SELECT Player.name, Bet.numbers, bd.hitNumbers, Bet.remaining, bd.hitSize, Bet.remainingSize "
                    + "FROM Bet "
                    + "INNER JOIN Player ON Player.player_ID = Bet.player_ID "
                    + "INNER JOIN Bet_Draw as bd ON bd.bet_ID = Bet.bet_ID AND bd.draw_ID = ?"
                    + "WHERE Bet.game_ID = (?) "
                    + "ORDER BY Player.name";
        }


        try {
            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, draw_ID);
            ps.setInt(2, game_ID);

            ResultSet rs = ps.executeQuery();

            md = rs.getMetaData();
            columnCount = md.getColumnCount();

            table = new PdfPTable(6);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell;
            if (ordr == 0) {
                cell = new PdfPCell(new Paragraph("Jogo " + game_ID + " sorteio " + draw_ID + " por acertos"));
            } else {
                cell = new PdfPCell(new Paragraph("Jogo " + game_ID + " sorteio " + draw_ID + " ord. alfabetica"));
            }
            cell.setColspan(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            table.addCell("Jogador");
            table.addCell("Aposta");
            table.addCell("Acertos");
            table.addCell("Jogador");
            table.addCell("Aposta");
            table.addCell("Acertos");

            while (rs.next()){
                table.addCell(rs.getString(1));
                Paragraph para;
                para = colorNumbers(rs.getString(2), rs.getString(4));
                table.addCell(para);
                Paragraph p = new Paragraph();
                Font f = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLUE);
                Chunk c;
                c = new Chunk(rs.getString(6), f);
                p.add(c);
                table.addCell(p);

                if (rs.next()){
                    table.addCell(rs.getString(1));
                    para = colorNumbers(rs.getString(2), rs.getString(4));
                    table.addCell(para);
                    p = new Paragraph();
                    f = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLUE);
                    c = new Chunk(rs.getString(6), f);
                    p.add(c);
                    table.addCell(p);
                } else {
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                }
            }

            LocalDate ld = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddLLLLyyyy");
            if (ordr == 0) {
                PdfWriter.getInstance(d, new FileOutputStream("./PDF/" + ld.format(formatter) + "SorteiosPorAcertos.pdf"));
            } else {
                PdfWriter.getInstance(d, new FileOutputStream("./PDF/" + ld.format(formatter) + "SorteiosOrdemAlfabetica.pdf"));
            }

            d.open();
            d.add(table);
            conn.close();

        } catch (SQLException | DocumentException | FileNotFoundException ex) {
            Logger.getLogger(ReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        d.close();

    }


    public static void winnersReport(int game_ID, int draw_ID){
        Document d = new Document();
        Rectangle rect = new Rectangle(PageSize.A4.rotate());
        d.setPageSize(rect);
        PdfPTable table = null;
        ResultSetMetaData md;
        int columnCount;

        String sql = "SELECT Player.name, Bet.numbers "
                + "FROM Bet "
                + "INNER JOIN games_Winners ON Bet.bet_ID = games_Winners.bet_ID "
                + "INNER JOIN Player ON Player.player_ID = Bet.player_ID "
                + "WHERE games_Winners.game_ID = (?) "
                + "ORDER BY Player.name";
        Connection conn;
        PreparedStatement ps;

        try {
            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            ResultSet rs = ps.executeQuery();

            md = rs.getMetaData();
            columnCount = md.getColumnCount();
            table = new PdfPTable(4);
            PdfPCell cell = new PdfPCell(new Paragraph("Vencedores Jogo " + game_ID));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            table.addCell("Jogador");
            table.addCell("Aposta");
            table.addCell("Jogador");
            table.addCell("Aposta");


            while (rs.next()){
                table.addCell("" + rs.getString(1));
                table.addCell("" + rs.getString(2));
                if (rs.next()){
                    table.addCell("" + rs.getString(1));
                    table.addCell("" + rs.getString(2));
                } else {
                    table.addCell("");
                    table.addCell("");
                }

            }
            conn.close();

            LocalDate ld = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddLLLLyyyy");
            PdfWriter.getInstance(d, new FileOutputStream("./PDF/" + ld.format(formatter) + "Vencedores.pdf"));
            d.open();
            d.add(table);


        } catch (SQLException | DocumentException | FileNotFoundException ex) {
            Logger.getLogger(ReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        d.close();
    }

    private static Paragraph colorNumbers(String numbers, String hits){
        Paragraph p = new Paragraph();

        String[] numAux = numbers.split(" ");
        List<Integer> nAux = new ArrayList<>();
        for (String s : numAux) {
            if (!"".equals(s))
                nAux.add(Integer.parseInt(s));
        }
        String[] hitAux = hits.split(" ");
        List<Integer> hAux = new ArrayList<>();
        for (String s : hitAux) {
            if (!s.equals(""))
                hAux.add(Integer.parseInt(s));
        }

        Font f = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
        Chunk c;

        for(int i: nAux){
            if(!hAux.contains(i)){
                c = new Chunk(Integer.toString(i), f);
                p.add(c);
                p.add(" ");
            } else {
                p.add("" + i + " ");
            }
        }

        return p;
    }
}