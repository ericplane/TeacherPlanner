package Data;

import Classes.SchoolClass;

import java.util.ArrayList;
import java.util.List;

public class SchoolClassManager {
    private final List<SchoolClassChangeListener> schoolClassChangeListeners = new ArrayList<>();
    DataManage dataManage;
    SchoolClass[] schoolClasses;
    String[] schoolClassNames;
    int selectedClassIndex = -1;

    public SchoolClassManager() {
        this.dataManage = new DataManage();
        this.schoolClasses = dataManage.loadClassData();

        if (this.schoolClasses != null) {
            if (this.schoolClasses.length > 0) {
                updateClassNames();
            }
        }
    }

    private void updateClassNames() {
        if (schoolClasses == null) {
            schoolClassNames = null;
            return;
        }

        String[] tempClassNames = new String[schoolClasses.length];
        int i = 0;
        for (SchoolClass schoolClass : schoolClasses) {
            tempClassNames[i] = schoolClass.className;
            i++;
        }
        schoolClassNames = tempClassNames;
    }

    public SchoolClass[] getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(SchoolClass[] schoolClasses) {
        this.schoolClasses = schoolClasses;
        dataManage.saveClassData(this.schoolClasses);
        updateClassNames();
        notifySchoolClassChangeListeners();
    }

    public SchoolClass getSchoolClass() {
        if (selectedClassIndex < 0) return null;
        return schoolClasses[selectedClassIndex];
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses[selectedClassIndex] = schoolClass;
        dataManage.saveClassData(this.schoolClasses);
        updateClassNames();
        notifySchoolClassChangeListeners();
    }

    public String[] getSchoolClassNames() {
        if (schoolClassNames == null) return new String[0];
        return schoolClassNames;
    }

    public void setSelectedClassIndex(int index) {
        this.selectedClassIndex = index;
        notifySchoolClassChangeListeners();
    }

    public void addSchoolClassChangeListener(SchoolClassChangeListener listener) {
        schoolClassChangeListeners.add(listener);
    }

    private void notifySchoolClassChangeListeners() {
        for (SchoolClassChangeListener listener : schoolClassChangeListeners) {
            listener.onSchoolClassChange();
        }
    }
}
