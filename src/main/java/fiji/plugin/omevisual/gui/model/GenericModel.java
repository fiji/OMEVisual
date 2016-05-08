/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fiji.plugin.omevisual.gui.model;

/**
 *
 * @author Hadrien Mary
 * @param <T>
 */
public abstract class GenericModel<T extends GenericModel<?>> {
        
    @Override
    public abstract String toString();
}
