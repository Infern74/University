package com.example.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.data.Day
import com.example.app.ui.home.adapter.WeekType
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.Locale

class HomeViewModel : ViewModel() {
    private val _currentDate = MutableLiveData(org.threeten.bp.LocalDate.now())
    val currentDate: LiveData<org.threeten.bp.LocalDate> = _currentDate

    private val _days = MutableLiveData<List<Day>>()
    val days: LiveData<List<Day>> = _days

    private val _weekType = MutableLiveData<WeekType>()
    val weekType: LiveData<WeekType> = _weekType

    private val _selectedDate = MutableLiveData<org.threeten.bp.LocalDate>()
    val selectedDate: LiveData<org.threeten.bp.LocalDate> = _selectedDate

    fun selectDate(date: org.threeten.bp.LocalDate) {
        _selectedDate.value = date
    }

    fun setDate(date: org.threeten.bp.LocalDate) {
        _currentDate.value = date
        _weekType.value = calculateWeekType(date)
    }

    private fun calculateWeekType(date: org.threeten.bp.LocalDate): WeekType {
        val weekOfMonth = date.get(org.threeten.bp.temporal.WeekFields.ISO.weekOfMonth())
        return if (weekOfMonth % 2 == 0) WeekType.LOWER else WeekType.UPPER
    }

    fun getCurrentWeekRange(date: LocalDate): String {
        val monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val sunday = monday.plusDays(6)
        return "${monday.dayOfMonth} - ${sunday.dayOfMonth} ${monday.month.getDisplayName(TextStyle.SHORT, Locale("ru"))}"
    }

    fun initFirstLoad() {
        val today = org.threeten.bp.LocalDate.now()
        _selectedDate.value = today
        _days.value?.firstOrNull { it.date == today }?.let {
            _days.value = _days.value?.map { day ->
                day.copy(isSelected = day.date == today)
            }
        }
    }

    fun loadDays(newDays: List<Day>) {
        _days.value = newDays
    }
}