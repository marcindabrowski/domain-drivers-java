package domaindrivers.smartschedule.allocation;

import domaindrivers.smartschedule.shared.timeslot.TimeSlot;
import domaindrivers.smartschedule.simulation.ProjectId;
import domaindrivers.smartschedule.simulation.SimulatedProject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

record PotentialTransfers(ProjectsAllocationsSummary summary, Map<ProjectAllocationsId, BigDecimal> earnings) {

    PotentialTransfers transfer(ProjectAllocationsId projectFrom, ProjectAllocationsId projectTo, AllocatedCapability capability, TimeSlot forSlot) {
        Allocations from = summary.projectAllocations().get(projectFrom);
        Allocations to = summary.projectAllocations().get(projectTo);
        if (from == null || to == null) {
            return this;
        }
        Allocations newAllocationsProjectFrom = from.remove(capability.allocatedCapabilityID(), forSlot);
        if (newAllocationsProjectFrom.equals(from)) {
            return this;
        }
        summary.projectAllocations().put(projectFrom, newAllocationsProjectFrom);
        Allocations newAllocationsProjectTo = to.add(new AllocatedCapability(capability.resourceId(), capability.capability(), forSlot));
        summary.projectAllocations().put(projectTo, newAllocationsProjectTo);
        return new PotentialTransfers(summary, earnings);
    }

    List<SimulatedProject> toSimulatedProjects() {
        return summary.projectAllocations().keySet().stream().map(project -> new SimulatedProject(ProjectId.from(project.id()), () -> earnings.get(project), getMissingDemands(project))).toList();
    }

    domaindrivers.smartschedule.simulation.Demands getMissingDemands(ProjectAllocationsId projectAllocationsId) {
        Demands allDemands = summary.demands().get(projectAllocationsId).missingDemands(summary.projectAllocations().get(projectAllocationsId));
        return new domaindrivers.smartschedule.simulation.Demands(
                allDemands
                        .all()
                        .stream()
                        .map(demand -> new domaindrivers.smartschedule.simulation.Demand(demand.capability(), demand.slot()))
                        .toList());
    }

}

