package com.theitfox.architecture.presentation.common.injection.abstracts;

/**
 * Created by btquanto on 11/10/2016.
 */
public interface HasComponent {
    /**
     * Gets component.
     *
     * @param <C> the type parameter
     * @return the component
     */
    <C> C getComponent();
}
