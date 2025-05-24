package com.algaworks.algasensors.temperature.management.api.model;

import io.hypersistence.tsid.TSID;

public record SensorInput(
        String name,
        String ip,
        String location,
        String protocol,
        String model
){
}
