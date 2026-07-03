import net.sf.jasperreports.engine.*;
import java.io.*;

public class CompileTest {
    public static void main(String[] args) {
        try {
            System.out.println("Compiling relatoriounidades.jrxml...");
            JasperCompileManager.compileReport(new FileInputStream("src/main/resources/relatorios/relatoriounidades.jrxml"));
            System.out.println("Compiling relatorioencomendas.jrxml...");
            JasperCompileManager.compileReport(new FileInputStream("src/main/resources/relatorios/relatorioencomendas.jrxml"));
            System.out.println("Success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
