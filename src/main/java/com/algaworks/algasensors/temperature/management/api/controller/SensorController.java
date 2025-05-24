package com.algaworks.algasensors.temperature.management.api.controller;

import com.algaworks.algasensors.temperature.management.api.model.SensorInput;
import com.algaworks.algasensors.temperature.management.api.model.SensorOutput;
import com.algaworks.algasensors.temperature.management.common.IdGenerator;
import com.algaworks.algasensors.temperature.management.domain.model.Sensor;
import com.algaworks.algasensors.temperature.management.domain.model.SensorId;
import com.algaworks.algasensors.temperature.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;

    @GetMapping("{sensorId}")
    public SensorOutput getOne(@PathVariable TSID sensorId){
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return getSensorOutput(sensor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput input){

        Sensor sensor = Sensor.builder()
                .id(new SensorId(IdGenerator.generateTSID()))
                .name(input.name())
                .ip(input.ip())
                .location(input.location())
                .protocol(input.protocol())
                .model(input.model())
                .enabled(false)
                .build();
        sensor = sensorRepository.saveAndFlush(sensor);
        return getSensorOutput(sensor);
    }

    private SensorOutput getSensorOutput(Sensor sensor) {
        return new SensorOutput(
                sensor.getId().getValue(),
                sensor.getName(),
                sensor.getIp(),
                sensor.getLocation(),
                sensor.getProtocol(),
                sensor.getModel(),
                false
        );
    }

}
