package domaindrivers.smartschedule.allocation;


import domaindrivers.smartschedule.shared.PrivateEvent;

import java.time.Instant;
import java.util.UUID;

record ProjectAllocationsDemandsScheduled(UUID uuid, ProjectAllocationsId projectId, Demands missingDemands, Instant occurredAt) implements PrivateEvent {

    ProjectAllocationsDemandsScheduled(ProjectAllocationsId projectId, Demands missingDemands, Instant occuredAt) {
        this(UUID.randomUUID(), projectId, missingDemands, occuredAt);
    }

}
