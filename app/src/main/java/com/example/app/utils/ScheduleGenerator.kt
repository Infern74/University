package com.example.app.utils

import com.example.app.data.Lesson
import org.threeten.bp.DayOfWeek

object ScheduleGenerator {
    private val schedule = mapOf(
        DayOfWeek.MONDAY to listOf(
            Lesson("09:50 - 11:25", "Лекция", "Математика", "Ауд. 301", "Корпус А", "Иванов И.И.")
        ),
        DayOfWeek.TUESDAY to listOf(
            Lesson("08:30 - 10:05", "Семинар", "Физика", "Ауд. 202", "Корпус Б", "Петров П.П."),
            Lesson("10:15 - 11:50", "Лабораторная", "Химия", "Лаб. 105", "Корпус В", "Сидорова С.С.")
        ),
        DayOfWeek.WEDNESDAY to listOf(
            Lesson("10:00 - 11:35", "Практика", "Информатика", "Ауд. 405", "Корпус Г", "Смирнов А.А.")
        ),
        DayOfWeek.THURSDAY to listOf(
            Lesson("09:00 - 10:35", "Лекция", "История", "Ауд. 101", "Корпус Д", "Кузнецов В.В.")
        ),
        DayOfWeek.FRIDAY to listOf(
            Lesson("11:00 - 12:35", "Семинар", "Экономика", "Ауд. 203", "Корпус Е", "Петрова О.О.")
        ),
        DayOfWeek.SATURDAY to emptyList(),
        DayOfWeek.SUNDAY to emptyList()
    )

    fun getLessonsForDay(dayOfWeek: DayOfWeek): List<Lesson> {
        return schedule[dayOfWeek] ?: emptyList()
    }
}