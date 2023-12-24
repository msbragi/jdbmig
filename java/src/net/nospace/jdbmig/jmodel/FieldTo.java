package net.nospace.jdbmig.jmodel;

/**
 *
 * @author Mmarco Sbragi
 */
public class FieldTo {
    private String name;
    private Integer type;
    private String renameTo;
    private Object dflt;

    public FieldTo() {
        renameTo = null;
        dflt = null;
    }

    public FieldTo(String name, int type) {
        this();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRenameTo() {
        return renameTo;
    }

    public void setRenameTo(String renameTo) {
        this.renameTo = renameTo;
    }

    public Object getDflt() {
        return dflt;
    }

    public void setDflt(Object dflt) {
        this.dflt = dflt;
    }
    
}
