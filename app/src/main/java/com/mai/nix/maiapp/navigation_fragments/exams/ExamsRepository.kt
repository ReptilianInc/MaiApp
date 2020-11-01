package com.mai.nix.maiapp.navigation_fragments.exams

import android.app.Application
import com.mai.nix.maiapp.MaiApp
import com.mai.nix.maiapp.helpers.Parser
import com.mai.nix.maiapp.model.ExamModel

class ExamsRepository {
    suspend fun getExamsWeb(group: String) = Parser.parseExams(group)
    suspend fun getExamsDb(application: Application) = (application as MaiApp).getDatabase().examDao.getExams()
    //FIXME: Метод генерации фейковых экзаменов, потому что на сайте их нет очень долго, возможно его стоит удалить
    fun getFakeExamsData(group: String) = arrayListOf(
            ExamModel("Дата1", "День1", "Время1", "Название1", "Препод1", "Комната1 - $group"),
            ExamModel("Дата2", "День2", "Время2", "Название2", "Препод2", "Комната2 - $group")
    )
}