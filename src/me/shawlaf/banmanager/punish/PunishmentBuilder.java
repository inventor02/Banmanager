package me.shawlaf.banmanager.punish;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Florian on 15.04.2017.
 */
public class PunishmentBuilder {
    public PunishmentType punishmentType;
    public PunishmentLengthBuilder punishmentLengthBuilder;
    public String reason, offender;
    public Set<UUID> multiPunish = new HashSet<>();
    
    public PunishmentBuilder(String offender, String reason) {
        this.reason = reason;
        this.offender = offender;
        this.punishmentLengthBuilder = new PunishmentLengthBuilder();
    }
    
    public PunishmentBuilder(String offender, String reason, PunishmentType punishmentType) {
        this(offender, reason);
        
        this.punishmentType = punishmentType;
    }
    
}
