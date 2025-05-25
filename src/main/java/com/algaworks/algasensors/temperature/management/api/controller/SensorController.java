package com.algaworks.algasensors.temperature.management.api.controller;

import com.algaworks.algasensors.temperature.management.api.model.SensorInput;
import com.algaworks.algasensors.temperature.management.api.model.SensorOutput;
import com.algaworks.algasensors.temperature.management.common.IdGenerator;
import com.algaworks.algasensors.temperature.management.domain.model.Sensor;
import com.algaworks.algasensors.temperature.management.domain.model.SensorId;
import com.algaworks.algasensors.temperature.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;

    @DeleteMapping("{sensorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TSID sensorId){
        sensorRepository.deleteById(new SensorId(sensorId));
    }

    @PutMapping("{sensorId}")
    public SensorOutput update(@PathVariable TSID sensorId, @RequestBody SensorInput input){
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));
        sensor.setName(input.name());
        sensor.setIp(input.ip());
        sensor.setLocation(input.location());
        sensor.setProtocol(input.protocol());
        sensor.setModel(input.model());
        sensor = sensorRepository.saveAndFlush(sensor);
        return getSensorOutput(sensor);
    }

    @PutMapping("{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable TSID sensorId){
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));
        sensor.setEnabled(true);
        sensorRepository.saveAndFlush(sensor);
    }

    @DeleteMapping("{sensorId}/enabled")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void disabled(@PathVariable TSID sensorId){
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));
        sensor.setEnabled(false);
        sensorRepository.saveAndFlush(sensor);
    }



    @GetMapping
    public Page<SensorOutput> searsh(@PageableDefault Pageable pageable) {
        //Page<Sensor> sensors = sensorRepository.findAll(pageable);
        return sensorRepository.findAll(pageable)
                .map(this::getSensorOutput);
    }

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
                sensor.getEnabled()
        );
    }

}
