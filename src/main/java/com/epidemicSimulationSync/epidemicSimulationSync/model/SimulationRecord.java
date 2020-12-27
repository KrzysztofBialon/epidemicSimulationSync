package com.epidemicSimulationSync.epidemicSimulationSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "DayRecords")
@Getter
@RequiredArgsConstructor
public class SimulationRecord
{
    @Id
    private String id;
    @JsonProperty(value = "ownerId")
    private final String ownerId;
    @JsonProperty(value = "records")
    private final List<SimulationDay> records;
}
