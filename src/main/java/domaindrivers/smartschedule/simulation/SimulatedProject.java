package domaindrivers.smartschedule.simulation;


import java.math.BigDecimal;
import java.util.function.Supplier;

record SimulatedProject(ProjectId projectId, Supplier<BigDecimal> value, Demands missingDemands) {

    BigDecimal calculateValue() {
        return value.get();
    }
}
