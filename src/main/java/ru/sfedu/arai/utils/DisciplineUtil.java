package ru.sfedu.arai.utils;

import ru.sfedu.arai.enums.DisciplineType;
import ru.sfedu.arai.model.CyclingDiscipline;
import ru.sfedu.arai.model.Discipline;
import ru.sfedu.arai.model.RunningDiscipline;
import ru.sfedu.arai.model.SwimmingDiscipline;

public class DisciplineUtil {
    public static Discipline getDisciplineBeanByType(DisciplineType disciplineType) throws Exception {
        return switch (disciplineType) {
            case RUNNING -> new RunningDiscipline();
            case CYCLING -> new CyclingDiscipline();
            case SWIMMING -> new SwimmingDiscipline();
            default -> throw new Exception("getDisciplineBeanByType error, incorrect discipline");
        };

    }
    public static DisciplineType disciplineTypeParser(String x) throws Exception {
        switch(x){
            case "SWIMMING": return DisciplineType.SWIMMING;
            case "RUNNING": return DisciplineType.RUNNING;
            case "CYCLING": return DisciplineType.CYCLING;
            case "ALL": return null;
            default: throw new Exception("disciplineTypeParser error, incorrect discipline");
        }
    }

    public static Discipline getDisciplineBean(Discipline discipline) throws Exception {
        return switch (discipline.getDisciplineType()) {
            case SWIMMING -> (SwimmingDiscipline) discipline;
            case RUNNING -> (RunningDiscipline) discipline;
            case CYCLING -> (CyclingDiscipline) discipline;
            default -> throw new Exception("getDisciplineBeanByType error, incorrect discipline");
        };
    }
}
