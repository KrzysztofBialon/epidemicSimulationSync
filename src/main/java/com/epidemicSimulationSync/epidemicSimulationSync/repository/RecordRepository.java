package com.epidemicSimulationSync.epidemicSimulationSync.repository;

import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationRecord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RecordRepository extends MongoRepository<SimulationRecord, String>
{
    SimulationRecord getById(String id);
    SimulationRecord getByOwnerId(String id);
}
