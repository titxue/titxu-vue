package com.titxu.cloud.common.core.domain;

import java.io.Serializable;

/**
 * Aggregate interface
 *


 **/
public interface Aggregate<T> extends Serializable {

    /**
     * Aggregate compare by identity, not by attributes.
     *
     * @param other The other Aggregate.
     * @return true if the identities are the same, regardless of other attributes.
     */
    boolean sameIdentityAs(T other);
}
