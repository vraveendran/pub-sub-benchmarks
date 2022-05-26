package com.vertexinc.frameworks.pubsub;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum KeyDistributorType {
    @JsonEnumDefaultValue
    /**
     * Key distributor that returns null keys to have default publish semantics
     */
    NO_KEY,

    /**
     * Genarate a finite number of "keys" and cycle through them in round-robin fashion
     */
    KEY_ROUND_ROBIN,

    /**
     * Random distribution based on System.nanoTime()
     */
    RANDOM_NANO,
}
