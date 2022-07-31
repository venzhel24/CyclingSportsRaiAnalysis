Module for analysing results and indicators of cycling sports based on csv, xml and jdbc data sources.

java -Denv=<Путь до файла конфигурации> -Dlog4j.configurationFile=<Путь до файла log4j2.xml> -jar <Название файла> <название дата провайдера(CSV, XML, JDBC)> <название метода>

Options

-ap <name> - Добавление участника
-dp <id> - Удаление участника
-gp <id> - Получение участника
-lp - Получение списка всех участников

-ar <name> - Добавление гонки
-dr <id> - Удаление гонки
-gr <id> - Получение гонки
-lr - Получение списка всех гонок

-ad <disciplineType, disciplinePlace, disciplineTime, distance, <Параметры, зависящие от disciplineType>> - Добавление записи по дисциплине
-dd <resultsId, disciplineType> - Удаление записи по дисциплине
-gd <resultsId, disciplineType> - Получение записи по дисциплине
-ld <disciplineType> - Получение списка записей всех дисциплин

-art <place, participantId, raceId, totalTime> - Добавление записи результата
-drt <participantId, raceId> - Удаление записи результата
-grt <resultsId> или <participantId, raceId> - Получение записи результата
-lrt <raceId> или <participantId>(не готово) - Получение списка записей всех результатов

-asw <raceType ,disciplineType, analysisStageWithoutWin> - анализ вероятности победы в гонке при победе по дисциплине disciplineType

-gap <raceType, participantId, disciplineType> - высчитывание среднего отставания участника с id participantId по дисциплине disciplineType 