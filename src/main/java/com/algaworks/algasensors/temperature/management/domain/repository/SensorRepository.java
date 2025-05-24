package com.algaworks.algasensors.temperature.management.domain.repository;

import com.algaworks.algasensors.temperature.management.domain.model.Sensor;
import com.algaworks.algasensors.temperature.management.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, SensorId> {
}
