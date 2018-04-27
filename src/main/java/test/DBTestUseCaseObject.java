package test;

import java.sql.Timestamp;
import java.util.HashSet;

/**
 * This class is used to generate objects used to test db features.
 */
public class DBTestUseCaseObject {
    private String charColumn;
    private long bigIntColumn;
    private float numericColumn;
    private Timestamp timeColumn;

    DBTestUseCaseObject() {
        setToDefault();
    }

    DBTestUseCaseObject(String charColumn, long bigIntColumn, float numericColumn, Timestamp timeColumn) {
        this.charColumn = charColumn;
        this.bigIntColumn = bigIntColumn;
        this.numericColumn = numericColumn;
        this.timeColumn = timeColumn;
    }

    public void setToDefault() {
        charColumn = "";
        bigIntColumn = 0;
        numericColumn = 0;
        timeColumn = new Timestamp(19000000);
    }

    public void setBigIntColumn(long bigIntColumn) {
        this.bigIntColumn = bigIntColumn;
    }

    public void setCharColumn(String charColumn) {
        this.charColumn = charColumn;
    }

    public void setNumericColumn(float numericColumn) {
        this.numericColumn = numericColumn;
    }

    public void setTimeColumn(Timestamp timeColumn) {
        this.timeColumn = timeColumn;
    }

    public String getCharColumn() {
        return this.charColumn;
    }

    public long getBigIntColumn() {
        return this.bigIntColumn;
    }

    public float getNumericColumn() {
        return this.numericColumn;
    }

    public Timestamp getTimeColumn() {
        return this.timeColumn;
    }

    public static HashSet<DBTestUseCaseObject> getTestObjectSet() {
        HashSet<DBTestUseCaseObject> testUseCaseObjects = new HashSet<>();
        int loopTime = 10;
        while(loopTime > 0) {
            String charColumnContent = String.format("No.%s DBTestUseCaseObject.", loopTime);
            long bigIntColumnContent = loopTime * 10000;
            float numericColumnContent = loopTime * 100;
            Timestamp timeColumnContent = new Timestamp(19000000 + loopTime * 10000);
            DBTestUseCaseObject testObj = new DBTestUseCaseObject(charColumnContent, bigIntColumnContent, numericColumnContent, timeColumnContent);
            testUseCaseObjects.add(testObj);
            loopTime--;
        }
        return testUseCaseObjects;
    }
}
