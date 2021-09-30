package sample.util;

public enum FUN {
    _3("FUN3-500-02", "1.4", 30, 2.5f, 50, "F1", "C3", "F3", "C4", "F4", "C19", "F19"),
    _3ZIP("FUN3-500-03", "1.4", 30, 2.5f, 50, "F1", "C3", "F3", "C4", "F4", "C19", "F19"),
    _6("FUN6-1000-02", "1.0.1", 60, 2.5f, 50, "F1", "C3", "F3", "C4", "F4", "C21", "F21"),
    _6ZIP("FUN6-1000-03", "1.0.1", 60, 2.5f, 50, "F1", "C3", "F3", "C4", "F4", "C21", "F21"),
    _10("FUN10-1500-02", "1.2.1", 90, 1f, 50, "F1", "C3", "F3", "C4", "F4", "C23", "F23"),
    _10ZIP("FUN10-1500-03", "1.2.1", 90, 1f, 50, "F1", "C3", "F3", "C4", "F4", "C23", "F23"),
    NONE(" ", " ", 0, 0, 0, "", "", "", "", "", "", "");

    private String name;
    private String HW;
    private int power;
    private float _12;
    private int _3v3;
    private String SNCell;
    private String nameCell;
    private String SNPCell;
    private String HWCell;
    private String fanModelCell;
    private String engineerCell;
    private String dateCell;

    FUN(String name, String HW, int power, float _12, int _3v3, String SNCell, String nameCell, String SNPCell, String HWCell, String fanModelCell, String engineerCell, String dateCell) {
        this.name = name;
        this.HW = HW;
        this.power = power;
        this._12 = _12;
        this._3v3 = _3v3;
        this.SNCell = SNCell;
        this.nameCell = nameCell;
        this.SNPCell = SNPCell;
        this.HWCell = HWCell;
        this.fanModelCell = fanModelCell;
        this.engineerCell = engineerCell;
        this.dateCell = dateCell;
    }

    public String getName() {
        return name;
    }

    public String getHW() {
        return HW;
    }

    public int getPower() {
        return power;
    }

    public float get_12() {
        return _12;
    }

    public int get_3v3() {
        return _3v3;
    }

    public String getSNCell() {
        return SNCell;
    }

    public String getNameCell() {
        return nameCell;
    }

    public String getSNPCell() {
        return SNPCell;
    }

    public String getHWCell() {
        return HWCell;
    }

    public String getFanModelCell() {
        return fanModelCell;
    }

    public String getEngineerCell() {
        return engineerCell;
    }

    public String getDateCell() {
        return dateCell;
    }
}