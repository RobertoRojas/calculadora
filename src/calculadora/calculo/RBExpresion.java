/*
 * Copyright (C) 2017 Roberto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package calculadora.calculo;

import calculadora.Utileria;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Clase que se encarga de hacer las gestiones del segmento de <i>expresion</i></p>
 * 
 * <p>Esta clase hereda de la clase abstracta de <i>{@link RBase}</i>.</p>
 *
 * @author Roberto
 * @version 1.0
 * @since 1.0
 * @see RBase
 */

public class RBExpresion extends RBase{
    
    /**
     * <p>Lista de elementos que contiene esta expresion.</p>
     * 
     * @since 1.0
     */
    
    private final List<RBase> elementos = new ArrayList<>();
    
    /**
     * <p>Lista de operadores a iterar con los elementos</p>
     * 
     * @since 1.0
     */
    
    private final List<REOperadores> operadores = new ArrayList<>();
    
    /**
     * <p>Texto con la expresion ingresada en la clase.</p>
     * 
     * @since 1.0
     */
    
    private final String expresion;
    
    /**
     * <p>Contructor unico de la case, cuando el objeto es construido se trata
     * la cadena ingresada.</p>
     * 
     * @since 1.0
     * @param expresion <i>Expresion a tratar.</i>
     */
    
    RBExpresion(String expresion) {
        this.expresion = expresion;
        tratarExpresion();
    }
    
    /**
     * <p>Este metodo secciona, clasifica y acomoda en las listas los objetos que 
     * se vayan requiriendo para resolver la operacion.</p>
     * 
     * @since 1.0
     */
    
    private void tratarExpresion() {
        if(!expresion.isEmpty() && (expresion.charAt(0)=='*' 
                || expresion.charAt(0)=='/'))throw new RobertianoException("Operador "
                        .concat(String.valueOf(expresion.charAt(0)))
                        .concat(" invalido para comenzar una expresion."));
        String aux = "0";
        for (int i = 0; i < expresion.length(); i++) {
            char caracter = expresion.charAt(i);
            switch(caracter){
                case '-':case '+':case '*':case '/':
                    if(!aux.isEmpty())insertarElemento(new RBNumero(aux));
                    operadores.add(REOperadores.obtenerOperador(caracter));
                    aux = "";                    
                    break;
                case '(':
                    int inicio = i + 1;
                    int cierre = getCierre(inicio);
                    insertarElemento(new RBExpresion(expresion.substring(inicio, cierre)));
                    aux = "";
                    i = cierre;
                    break;
                default:
                    if(Utileria.isNumero(caracter) || caracter == '.'){
                        aux = aux.concat(String.valueOf(caracter));
                    } else {
                        switch(caracter){
                            case ')':
                                throw new RobertianoException("Los parencecis no abren de manera correcta.");
                            default:
                                throw new RobertianoException("El caracter en la posicion "
                                    .concat(String.valueOf(i)).concat(" de la expresion ")
                                    .concat(expresion).concat(" no se pudo tratar"));
                        }
                                
                    }
            }
        }      
        if(!aux.isEmpty())insertarElemento(new RBNumero(aux));
        insertarElemento(new RBNumero());
        operadores.add(REOperadores.SUMA);
    }
    
    /**
     * <p>Este metodo nos indica donde cierra un parentecisis dado en cierto
     * segmento de la expresion.</p>
     * 
     * <p>En caso de no encontrar un cierre valido retorna una excepcion en
     * tiempo de ejecucion. Tambien retornada una excepcion si el indice no se
     * mueve del inicio.</p>
     * 
     * @since 1.0
     * 
     * @param inicio <i>Indice donde se va a comenzar a buscar.</i>
     * @return <i>Indice del cierre de una subexpresion.</i>
     */
    
    private int getCierre(int inicio){
        int apertura = 1;
        int cierre = 0;
        for (int i = inicio; i < expresion.length(); i++) {
            char caracter = expresion.charAt(i);
            if(caracter=='(')apertura++;
            if(caracter==')')cierre++;
            if(apertura==cierre){
                if(i==inicio)throw new RobertianoException("Se ingreso un parentesis vacio");
                return i;
            }
        }
        throw new RobertianoException("Los parencecis no cierran de manera correcta.");
    }
    
    /**
     * <p>Este metodo inserta un elemento en la lista de elementos, en caso de 
     * que el operador sea una multiplicacion o division genera un operacion, con
     * las reglas establecidas para ello en la notacion <i>Robertiana</i>.</p>
     * 
     * @since 1.0
     * @param elemento <i>Elemento a ingresar en las lista.</i> 
     */
    
    private void insertarElemento(RBase elemento){
        if(!operadores.isEmpty() && (operadores.get(operadores.size() - 1)==REOperadores.MULTIPLICACION 
                || operadores.get(operadores.size()-1)==REOperadores.DIVISION)){
            RBase anterior = elementos.get(elementos.size() - 1);
            elementos.remove(anterior);
            elementos.add(new RBOperacion(anterior, elemento, operadores.get(operadores.size() - 1)));
            operadores.remove(operadores.size() - 1);
        } else elementos.add(elemento);
    }
    
    /**
     * <p>Genera una lista espejo de los elementos que estan en la lista de elementos, 
     * y con ellos hace las operaciones requeridas sin modificar el orden de los 
     * elementos originales.</p>
     * 
     * @since 1.0
     * @return <i>Copia de la lista de elementos.</i>
     */
    
    private List<RBase> copiarElementos(){
        List<RBase> aux = new ArrayList<>();
        elementos.stream().forEach((elemento) -> {
            aux.add(elemento);
        });
        return aux;
    }
    /**
     * <p>Retorna la expresion en notacion <i>Robertiana.</i></p>
     * 
     * @since 1.0
     * @return <i>Cadena con la notacion robertianan de la expresion</i>
     */
    @Override
    String showRobertiano() {
        String aux = "(";
        for(RBase elemento:elementos)aux = aux.concat("[")
                .concat(elemento.showRobertiano()).concat("]");
        aux = aux.concat(" ");
        for(REOperadores operador:operadores)aux = aux.concat(operador.toString());
        return aux.concat(")");
    }
    
    /**
     * <p>Obtiene el valor de las operaciones en la expresion.</p>
     * 
     * @since 1.0
     * @return <i>Resultado de la expresion.</i>
     */
    
    @Override
    double getValor() {
        List<RBase> aux = copiarElementos();
        operadores.stream().forEach((operador) -> {
            if(aux.size()<2)throw new RobertianoException("Error en la configuracion de las operaciones, favor de revisar.");
            RBase a = aux.get(0);
            RBase b = aux.get(1);
            aux.remove(a);
            aux.remove(b);
            aux.add(0,new RBNumero(operador.operar(a, b)));
        });
        return aux.get(0).getValor();
    }
}
