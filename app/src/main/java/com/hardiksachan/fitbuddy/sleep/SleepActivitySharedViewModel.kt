package com.hardiksachan.fitbuddy.sleep

import android.app.Application
import androidx.lifecycle.*
import com.hardiksachan.fitbuddy.database.SleepNight
import com.hardiksachan.fitbuddy.formatNights
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.launch

class SleepActivitySharedViewModel(application: Application) : AndroidViewModel(application) {

    val fitBuddyRepository = FitBuddyRepository(application)

    val nights = fitBuddyRepository.allSleepNights

    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    private var tonight = MutableLiveData<SleepNight?>()

    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }

    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private var sleepQualityNight: SleepNight? = null

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromRepo()
        }
    }

    private suspend fun getTonightFromRepo(): SleepNight? {
        var night = fitBuddyRepository.getTonight()
        if (night?.endTimeMilli != night?.startTimeMilli) {
            night = null
        }
        return night
    }

    fun onStartTracking() {
        viewModelScope.launch {
            val newNight = SleepNight()
            fitBuddyRepository.insertSleepNight(newNight)
            tonight.value = getTonightFromRepo()
        }
    }

    fun onStopTracking() {
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            fitBuddyRepository.updateSleepNight(oldNight)

            sleepQualityNight = oldNight
        }
        tonight.value = null
    }

    fun onClear() {
        viewModelScope.launch {
            fitBuddyRepository.clearsleepNight()
            tonight.value = null
        }
    }

    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch {
            val tonight = sleepQualityNight ?: return@launch
            tonight.sleepQuality = quality
            fitBuddyRepository.updateSleepNight(tonight)
        }
        _navigateToSleepTracker.value = true
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SleepActivitySharedViewModel::class.java)) {
                return SleepActivitySharedViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}