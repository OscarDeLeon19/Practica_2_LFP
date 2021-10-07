package analisis;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Automata {

    int[][] matriz = new int[6][6];
    Error error = new Error();

    public Automata() {
        definirMatriz();
    }

    /**
     * Enum que contiene los tipos de tokens
     */
    public enum Token {
        IDENTIFICADOR,
        NUMERO,
        DECIMAL,
        PUNTUACION,
        OPERADOR,
        AGRUPACION,
        ERROR,
    }

    /**
     * Matriz del automata que se usara para este lenguaje
     */
    public void definirMatriz() {
        matriz[0][0] = 1;
        matriz[0][1] = 1;
        matriz[0][2] = -1;
        matriz[0][3] = -1;
        matriz[0][4] = -1;
        matriz[0][5] = -1;

        matriz[1][0] = 2;
        matriz[1][1] = 1;
        matriz[1][2] = 2;
        matriz[1][3] = -1;
        matriz[1][4] = 5;
        matriz[1][5] = 5;

        matriz[2][0] = 3;
        matriz[2][1] = -1;
        matriz[2][2] = 4;
        matriz[2][3] = -1;
        matriz[2][4] = -1;
        matriz[2][5] = -1;

        matriz[3][0] = 3;
        matriz[3][1] = -1;
        matriz[3][2] = -1;
        matriz[3][3] = -1;
        matriz[3][4] = -1;
        matriz[3][5] = -1;

        matriz[4][0] = 3;
        matriz[4][1] = -1;
        matriz[4][2] = -1;
        matriz[4][3] = -1;
        matriz[4][4] = -1;
        matriz[4][5] = -1;

        matriz[5][0] = 3;
        matriz[5][1] = -1;
        matriz[5][2] = -1;
        matriz[5][3] = -1;
        matriz[5][4] = -1;
        matriz[5][5] = -1;

    }

    /**
     * Analiza individualmente las palabras obtenidas del analizador. Y agrega
     * los movimientos al area de movimientos.
     *
     * @param palabras Las palabras que se van a analizar
     * @param AreaMovimientos El Area donde se van a agregar los movimientos
     * @param posiciones Las posiciones de los lexemas
     */
    public void recorrerToken(ArrayList<String> palabras, JTextArea AreaMovimientos, int[][] posiciones) {
        error.limpiar();
        ArrayList<String> movimientos = new ArrayList<>();
        Token[] tokens = new Token[palabras.size()];
        for (int i = 0; i < palabras.size(); i++) {
            String palabra = palabras.get(i);
            analizarToken(palabra, tokens, i, movimientos);
        }
        boolean errores = false;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == Token.ERROR) {
                String texto = "Lexema: " + palabras.get(i) + " | Fila: " + posiciones[i][0] + " | Columna: " + posiciones[i][1];
                error.agregarError(texto);
                errores = true;
            }
        }
        for (int i = 0; i < movimientos.size(); i++) {
            AreaMovimientos.append(movimientos.get(i));
            AreaMovimientos.append("\n");
        }
        if (errores == true) {
            JOptionPane.showMessageDialog(null, "Hay errores en el programa");
            error.setVisible(true);

        } else {
            Reporte reporte = new Reporte(palabras, tokens, posiciones);

        }
    }

    /**
     * Analiza caracter por caracter de una lapabra y recorre la matriz para
     * saber que tipo de token es
     *
     * @param palabra La palabra que se analizara
     * @param tokens El arreglo de tokens
     * @param numero El numero de token que corresponde
     * @param movimientos La lista de movimientos del automata
     */
    public void analizarToken(String palabra, Token[] tokens, int numero, ArrayList<String> movimientos) {
        char[] caracteres = palabra.toCharArray();
        int no_caracter = -1;
        int estado_actual = 0;
        movimientos.add("Lexema: " + palabra);
        for (int i = 0; i < caracteres.length; i++) {
            no_caracter = obtenerCaracter(caracteres[i]);
            if (no_caracter >= 0) {
                int estado_nuevo = matriz[no_caracter][estado_actual];
                String movimiento = "Con el caracter " + caracteres[i] + " Se recorrio del estado S" + estado_actual + " al estado S" + estado_nuevo;
                movimientos.add(movimiento);
                estado_actual = estado_nuevo;
                if (estado_actual < 0) {
                    tokens[numero] = Token.ERROR;
                    i = caracteres.length;
                }
            } else {
                String movimiento = "El caracter:  " + caracteres[i] + " No es reconocido";  
                movimientos.add(movimiento);
                estado_actual = -1;
                i = caracteres.length;
            }
        }

        if (estado_actual == 1) {
            tokens[numero] = Token.IDENTIFICADOR;
        } else if (estado_actual == 2) {
            tokens[numero] = Token.NUMERO;
        } else if (estado_actual == 3) {
            if (no_caracter == 2 || no_caracter == 3){
                tokens[numero] = Token.PUNTUACION;
            } else if (no_caracter == 4){
                tokens[numero] = Token.OPERADOR;
            } else if (no_caracter == 5){
                tokens[numero] = Token.AGRUPACION;
            } else {
                tokens[numero] = Token.ERROR;
            }
        } else if (estado_actual == 4) {
            tokens[numero] = Token.ERROR;
        } else if (estado_actual == 5) {
            tokens[numero] = Token.DECIMAL;
        } else if (estado_actual == -1) {
            tokens[numero] = Token.ERROR;
        }

    }
    /**
     * Obtiene el numero del tipo de caracter que corresponda segun el simbolo
     * @param caracter El caracter que se analizara
     * @return El numero de tipo de caracter
     */
    public int obtenerCaracter(char caracter) {
        if (Character.isLetter(caracter)) {
            if (caracter == 'ñ') {
                return -1;
            } else {
                return 0;
            }
        } else if (Character.isDigit(caracter)) {
            return 1;
        } else if (caracter == '.') {
            return 2;
        } else if (caracter == ',') {
            return 3;
        } else if (caracter == ';') {
            return 3;
        } else if (caracter == ':') {
            return 3;
        } else if (caracter == '+') {
            return 4;
        } else if (caracter == '-') {
            return 4;
        } else if (caracter == '*') {
            return 4;
        } else if (caracter == '/') {
            return 4;
        } else if (caracter == '%') {
            return 4;
        } else if (caracter == '(') {
            return 5;
        } else if (caracter == ')') {
            return 5;
        } else if (caracter == '[') {
            return 5;
        } else if (caracter == ']') {
            return 5;
        } else if (caracter == '{') {
            return 5;
        } else if (caracter == '}') {
            return 5;
        } else {
            return -1;
        }
    }
}
