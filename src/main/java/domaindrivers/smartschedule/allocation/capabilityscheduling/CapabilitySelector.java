package domaindrivers.smartschedule.allocation.capabilityscheduling;



import domaindrivers.smartschedule.shared.capability.Capability;

import java.io.Serializable;
import java.util.*;

record CapabilitySelector(Set<Capability> capabilities, SelectingPolicy selectingPolicy) implements Serializable {


    enum SelectingPolicy {
        ALL_SIMULTANEOUSLY, ONE_OF_ALL
    }

    static CapabilitySelector canPerformAllAtTheTime(Set<Capability> capabilities) {
        return new CapabilitySelector(capabilities, SelectingPolicy.ALL_SIMULTANEOUSLY);
    }

    static CapabilitySelector canPerformOneOf(Set<Capability> capabilities) {
        return new CapabilitySelector(capabilities, SelectingPolicy.ONE_OF_ALL);
    }

    public static CapabilitySelector canJustPerform(Capability capability) {
        return new CapabilitySelector(Set.of(capability), SelectingPolicy.ONE_OF_ALL);
    }

    boolean canPerform(Capability capability) {
        return capabilities.contains(capability);
    }

    boolean canPerform(Set<Capability> capabilities) {
        if (capabilities.size() == 1) {
            return new HashSet<>(this.capabilities).containsAll(capabilities);
        }
        return selectingPolicy.equals(SelectingPolicy.ALL_SIMULTANEOUSLY) &&
                new HashSet<>(this.capabilities).containsAll(capabilities);
    }


}

