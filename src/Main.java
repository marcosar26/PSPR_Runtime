import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class Main {
    static boolean comprobarComando(String comando) {
        return !comando.isBlank() || !comando.isEmpty();
    }

    static File comprobarRuta(String ruta) {
        if (ruta.isEmpty() || ruta.isBlank()) {
            System.out.println("Ruta inválida.");
            return null;
        }
        File file = new File(ruta);
        if (!file.exists() || !file.isDirectory()) {
            System.out.println("Ruta inválida.");
            return null;
        }
        return file;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String comando = args[0];
        if (!comprobarComando(comando)) {
            System.out.println("No se ha especificado ningún comando.");
            return;
        }

        System.out.print("Introduce la ruta: ");
        String ruta;
        File rutaDir;
        do {
            ruta = sc.next();
            rutaDir = comprobarRuta(ruta);
        } while (rutaDir != null);

        BufferedReader reader;
        try {
            String[] cmd = new String[]{"cmd.exe", "/c", comando};
            InputStream stream = Runtime.getRuntime().exec(cmd, null, new File(ruta)).getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
        } catch (IOException e) {
            System.out.println("Error al ejecutar el comando.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        reader.lines().forEach(str -> sb.append(str).append(System.lineSeparator()));

        System.out.println(sb);

        System.out.print("¿Desea guardar la información? (si/no): ");
        String res = sc.next();

        if (res.equalsIgnoreCase("si")) {
            File file = new File(ruta + "/" + comando + ".txt");

            if (file.exists()) {
                file.delete();
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Error al crear el fichero.");
                }
            }

            try {
                Files.write(file.toPath(), sb.toString().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println("Error al escribir en el fichero.");
            }
        }
    }
}