package com.example.app.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.data.Day
import com.example.app.data.Lesson
import com.example.app.databinding.FragmentHomeBinding
import com.example.app.ui.home.adapter.DayAdapter
import com.example.app.ui.home.adapter.LessonAdapter
import com.example.app.ui.home.adapter.WeekType
import com.example.app.utils.ScheduleGenerator
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setupWeekNavigation()
        setupObservers()
        setupCalendar()
        loadInitialData()

        viewModel.days.value?.firstOrNull { it.isSelected }?.date?.let {
            loadLessonsForDay(it)
        }
    }

    private fun setupWeekNavigation() {
        binding.btnPrevWeek.setOnClickListener {
            viewModel.currentDate.value?.minusWeeks(1)?.let {
                viewModel.setDate(it)
            }
        }

        binding.btnNextWeek.setOnClickListener {
            viewModel.currentDate.value?.plusWeeks(1)?.let {
                viewModel.setDate(it)
            }
        }
    }

    private fun loadInitialData() {
        viewModel.setDate(org.threeten.bp.LocalDate.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupObservers() {
        viewModel.currentDate.observe(viewLifecycleOwner) { date ->
            updateMonthHeader(date)
            loadDaysForWeek(date) // Загружаем дни недели, а не месяца
        }

        viewModel.days.observe(viewLifecycleOwner) { days ->
            binding.rvCalendar.adapter = DayAdapter(days) { selectedDate ->
                updateSelectedDay(selectedDate)
                loadLessonsForDay(selectedDate)
            }
        }

        viewModel.weekType.observe(viewLifecycleOwner) { weekType ->
            binding.tvWeekType.text = when (weekType) {
                WeekType.UPPER -> "Верхняя неделя"
                WeekType.LOWER -> "Нижняя неделя"
            }
        }
    }

    private fun loadDaysForWeek(date: org.threeten.bp.LocalDate) {
        val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val today = org.threeten.bp.LocalDate.now()

        val days = (0..6).map { startOfWeek.plusDays(it.toLong()) }
            .map { localDate ->
                Day(
                    date = localDate,
                    isSelected = localDate == today
                )
            }
        viewModel.loadDays(days)

        // Загружаем расписание для сегодняшнего дня
        loadLessonsForDay(today)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateMonthHeader(date: org.threeten.bp.LocalDate) {
        val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("MMMM yyyy", Locale("ru"))
        binding.tvMonthYear.text = date.format(formatter)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDaysForMonth(date: LocalDate) {
        val firstDay = date.withDayOfMonth(1)
        val lastDay = date.withDayOfMonth(date.lengthOfMonth())

        val days = generateDateRange(firstDay, lastDay).map { localDate ->
            Day(
                date = localDate,
                isSelected = localDate == LocalDate.now()
            )
        }

        viewModel.loadDays(days)
    }

    private fun generateDateRange(
        start: org.threeten.bp.LocalDate,
        end: org.threeten.bp.LocalDate
    ): List<org.threeten.bp.LocalDate> {
        val daysBetween = ChronoUnit.DAYS.between(start, end)
        if (daysBetween < 0) return emptyList() // Обработка случая, когда start > end
        return (0L..daysBetween).map { offset ->
            start.plusDays(offset)
        }
    }

    private fun setupCalendar() {
        binding.rvCalendar.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.days.observe(viewLifecycleOwner) { days ->
            binding.rvCalendar.adapter = DayAdapter(days) { selectedDate ->
                updateSelectedDay(selectedDate)
                loadLessonsForDay(selectedDate)
            }
        }
    }

    private fun updateSelectedDay(selectedDate: org.threeten.bp.LocalDate) {
        val updatedDays = viewModel.days.value?.map { day ->
            day.copy(isSelected = day.date == selectedDate)
        }
        updatedDays?.let {
            viewModel.loadDays(it)
            binding.rvCalendar.adapter?.notifyDataSetChanged()
            loadLessonsForDay(selectedDate) // Явное обновление расписания
        }
    }

    private fun loadLessonsForDay(selectedDate: org.threeten.bp.LocalDate) {
        val dayOfWeek = selectedDate.dayOfWeek
        val lessons = ScheduleGenerator.getLessonsForDay(dayOfWeek)
        println("Расписание для $dayOfWeek: ${lessons.size} пар")
        binding.rvSchedule.apply {
            adapter = LessonAdapter(lessons)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}