package sample.util;

public enum Crate {
    V3("V3-02", 3, "10", "1.3.6.1.4.1.39433.1.2.3", "1.5", FUN._10, "F1", "C3", "F4", "C4", "C22", "F22"),
    V6("V6-02", 7, "9", "1.3.6.1.4.1.39433.1.2.4", "1.5", FUN._6, "F1", "C3", "F4", "C4", "C26", "F26"),
    V10("V10-02", 13, "8", "1.3.6.1.4.1.39433.1.2.5", "1.4", FUN._3, "F1", "C3", "F4", "C4", "C32", "F32");

    private String name;
    private int slotAmount;
    private String code;
    private String SNMP;
    private String BPRev;
    private FUN fun;
    private String SNCell;
    private String nameCell;
    private String SNBPCell;
    private String BPrevCell;
    private String engineerCell;
    private String dateCell;

    Crate(String name, int slotAmount, String code, String SNMP, String BPRev, FUN fun, String SNCell, String nameCell, String SNBPCell, String BPRevCell, String engineerCell, String dateCell) {
        this.name = name;
        this.slotAmount = slotAmount;
        this.code = code;
        this.SNMP = SNMP;
        this.BPRev = BPRev;
        this.fun = fun;
        this.SNCell = SNCell;
        this.nameCell = nameCell;
        this.SNBPCell = SNBPCell;
        this.BPrevCell = BPRevCell;
        this.engineerCell = engineerCell;
        this.dateCell = dateCell;
    }

    public String getName() {
        return name;
    }

    public int getSlotAmount() {
        return slotAmount;
    }

    public String getCode() {
        return code;
    }

    public String getSNMP() {
        return SNMP;
    }

    public String getBPRev() {
        return BPRev;
    }

    public FUN getFun() {
        return fun;
    }

    public String getSNCell() {
        return SNCell;
    }

    public String getNameCell() {
        return nameCell;
    }

    public String getSNBPCell() {
        return SNBPCell;
    }

    public String getBPrevCell() {
        return BPrevCell;
    }

    public String getEngineerCell() {
        return engineerCell;
    }

    public String getDateCell() {
        return dateCell;
    }
}