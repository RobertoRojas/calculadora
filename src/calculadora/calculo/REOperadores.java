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

/**
 * <p>Objeto enumerable que nos indica las operaciones validas ingresadas en el 
 * sistema.</p>
 * 
 * @author Roberto
 * @version 1.0
 * @since 1.0
 */

public enum REOperadores {
    
    /**
     * <p>Las operaciones ingresadas son(entre parentesis se indica el caracter
     * que esta asociado a la operacion):</p>
     * <ul>
     *  <li>SUMA(+)</li>
     *  <li>RESTA(-)</li>
     *  <li>DIVISION(/)</li>
     *  <li>MULTIPLICACION(*)</li>
     * </ul>
     * 
     * @since 1.0
     */
    
    SUMA('+'),
    RESTA('-'),
    MULTIPLICACION('*'),
    DIVISION('/');
    
    /**
     * <p>Esta variable guarda el caracter que identifica la operacion.</p>
     * 
     * @since 1.0
     */
    
    private final char identificador;
    
    /**
     * <p>Constructor privado de las operaciones.</p>
     * 
     * @since 1.0
     * @param identificador <i>Caracter que identifica la operacion.</i>
     */
    
    private REOperadores(char identificador){
        this.identificador = identificador;
    }
    
    /**
     * <p>Se sobreescribe el metodo <i>{@link Object#toString()}</i> para que
     * cuando se mande a llamar retorne el identificador asociado a el operador.</p>
     * 
     * @since 1.0
     * @return <i>Identificador de la operacion.</i>
     */
    
    @Override
    public String toString(){
        return String.valueOf(identificador);
    }
    
    /**
     * <p>Opera dos valores que son ingresados, dependiendo de la operacion en la 
     * que se esta mandando a llamar, se realiza una u otra operacion.</p>
     * 
     * @since 1.0
     * @param a <i>Primer valor, numerador en una operacion de tipo division.</i>
     * @param b <i>Segundo valor, denominador en una operacion de tipo division</i>
     * @return <i>Resultado de la operacion.</i>
     */
    
    double operar(RBase a, RBase b){
        switch(this){
            case SUMA:
                return a.getValor() + b.getValor();
            case RESTA:
                return a.getValor() - b.getValor();
            case MULTIPLICACION:
                return a.getValor() * b.getValor();
            default:
                return a.getValor() / b.getValor();
        }
    }
    
    /**
     * <p>Retorna el tipo de operacion que esta emparejado con el caracter ingresado.</p>
     * 
     * <p>Si no encuentra ninguna coincidencia se manda una excepcion de tiempo
     * de ejecucion.</p>
     * 
     * @since 1.0
     * @param identificador <i>Caracter con el cual se intenta operar una expresion.</i>
     * @return <i>La operacion que coincida con el identificador ingresado.</i>
     */
    
    static REOperadores obtenerOperador(char identificador){
        switch(identificador){
            case '+':
                return SUMA;
            case '-':
                return RESTA;
            case '*':
                return MULTIPLICACION;
            case '/':
                return DIVISION;
            default:
                throw new RobertianoException("El caracter ingresado no coincide con ningun objeto valido: "
                        .concat(String.valueOf(identificador)));
        }
    }
}