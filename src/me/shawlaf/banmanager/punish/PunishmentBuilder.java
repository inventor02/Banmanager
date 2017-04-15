package me.shawlaf.banmanager.punish;

/**
 * Created by Florian on 15.04.2017.
 */
public class PunishmentBuilder {
    public PunishmentType punishmentType;
    public PunishmentLengthBuilder punishmentLengthBuilder;
    public String reason, offender;
    
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
