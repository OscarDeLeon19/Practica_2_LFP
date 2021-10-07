package analisis;

import java.util.ArrayList;
import javax.swing.JTextArea;

public class Analizador {

    Automata automata = new Automata();

    public Analizador() {

    }
    /**
     * Metodo para analizar el texto y obtener las palabras que contengan el texto. LLama al automata para obtener el tipo de token
     * @param texto El texto del JtextArea
     * @param AreaMovimientos El area donde se guardan los movimientos del automata
     */
    public void analizar(String texto, JTextArea AreaMovimientos) {
        ArrayList<Integer> numeros = new ArrayList<>();
        texto = " " + texto;
        int x = 0;
        for (int i = 0; i < texto.length(); i++) {
            if (" ".equals(texto.substring(i, i + 1)) || "\n".equals(texto.substring(i, i + 1))) {
                if (i + 1 != texto.length()) {
                    if (" ".equals(texto.substring(i + 1, i + 2)) || "\n".equals(texto.substring(i + 1, i + 2))) {

                    } else {
                        if (x == 0) {
                            numeros.add(i + 1);
                        } else {
                            numeros.add(i);
                            numeros.add(i + 1);
                        }
                        x++;
                    }
                }
            }
        }
        if (x == 0) {
            numeros.add(0);
        }
        numeros.add(texto.length());
        ArrayList<String> palabras = new ArrayList<>();
        for (int i = 0; i < numeros.size(); i++) {
            String nueva_palabra = texto.substring(numeros.get(i), numeros.get(i + 1));
            String arreglo = arreglarCadena(nueva_palabra);
            palabras.add(arreglo);
            i++;
        }
        int[][] posiciones = obtenerPosicion(texto, palabras);
        automata.recorrerToken(palabras, AreaMovimientos, posiciones);

    }
    /**
     * Obtiene la posicion de un lexema
     * @param texto El texto del jtextArea
     * @param palabras Las palabras obtenidas del analizador
     * @return El arreglo con la fila y columna
     */
    public int[][] obtenerPosicion(String texto, ArrayList<String> palabras) {
        int[][] posiciones = new int[palabras.size()][2];
        int fila = 1;
        int columna = 0;
        int palabra = 0;
        for (int i = 0; i < texto.length(); i++) {
            String lexema = "";
            lexema = palabras.get(palabra);
            if ("\n".equals(texto.substring(i, i + 1))) {
                fila++;
                columna = 0;
            }
            if (i + lexema.length() <= texto.length()) {
                if (texto.substring(i, i + lexema.length()).equals(lexema)) {
                    posiciones[palabra][0] = fila;
                    posiciones[palabra][1] = columna;
                    palabra++;
                    if (palabra == palabras.size()) {
                        i = texto.length();
                    } else {
                        i += lexema.length()-1;
                        columna += lexema.length()-1;
                    }
                }
            }
            columna++;
        }
        return posiciones;
    }
    /**
     * Elimina los espacios en blanco de las palabras
     * @param palabra La palabra a arreglar
     * @return La palabra arreglada
     */
    public String arreglarCadena(String palabra) {
        String nueva_palabra = "";
        boolean arreglo = false;
        int inicio = 0;
        int fin = 0;
        for (int i = 0; i < palabra.length(); i++) {
            if (" ".equals(palabra.substring(i, i + 1))) {
                inicio = i + 1;
                arreglo = true;
            } else {
                i = palabra.length();
            }
        }
        String nuevo = palabra.substring(inicio, palabra.length());
        char caracteres[] = nuevo.toCharArray();
        for (int i = 0; i < caracteres.length; i++) {
            if (Character.isWhitespace(caracteres[i])) {
                fin = i;
                arreglo = true;
                i = caracteres.length;
            }
        }
        if (arreglo == true) {
            nueva_palabra = palabra.substring(inicio, inicio + fin);
        } else {
            nueva_palabra = palabra;
        }
        return nueva_palabra;
    }

}
