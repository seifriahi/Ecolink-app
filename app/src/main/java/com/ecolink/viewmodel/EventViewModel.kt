package com.ecolink.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ecolink.models.Event
import androidx.lifecycle.viewModelScope
import com.ecolink.Service.RetrofitInstance
import kotlinx.coroutines.launch
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class EventViewModel : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events
    private val _selectedEvent = MutableLiveData<Event>()
    val selectedEvent: LiveData<Event> = _selectedEvent

    fun fetchEvents() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.eventService.getEvents()

                val eventList = response.list


                for (event in eventList) {
                    Log.d("EventViewModel", "Event: ${event.eventName}")
                }

                if (eventList.isNotEmpty()) {
                    _events.value = eventList
                } else {
                    Log.e("EventViewModel", "La liste des événements est vide")
                }

            } catch (e: Exception) {
                Log.e("EventViewModel", "Erreur lors de la récupération des événements: ${e.message}")
                // Gérer l'erreur ici si nécessaire
            }
        }

    }
    fun fetchEvents2() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.eventService.getEvents2()

                val eventList = response.list


                for (event in eventList) {
                    Log.d("EventViewModel", "Event: ${event.eventName}")
                }

                if (eventList.isNotEmpty()) {
                    _events.value = eventList
                } else {
                    Log.e("EventViewModel", "La liste des événements est vide")
                }

            } catch (e: Exception) {
                Log.e(
                    "EventViewModel",
                    "Erreur lors de la récupération des événements: ${e.message}"
                )
                // Gérer l'erreur ici si nécessaire
            }
        }
        fun checkEventDates(events: List<Event>) {
            for (event in events) {
                val dateString =
                    event.eventDate // Assurez-vous de récupérer la date de l'événement correctement
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                val dateEvenement = dateFormat.parse(dateString) ?: continue

                val dateActuelle = Date()

                if (dateActuelle >= dateEvenement) {


                    Log.d("Notification", "Votre événement ${event.eventName} est en cours !")
                    // Ici, vous pouvez ajouter le code pour envoyer la notification à l'utilisateur
                }
            }
        }

        fun fetchEventDetails(eventId: String?) {
            eventId?.let { id ->
                viewModelScope.launch {
                    try {
                        val response = RetrofitInstance.eventService.getEventDetails(id)

                        // Update LiveData for the selected event
                        _selectedEvent.value = response
                    } catch (e: Exception) {
                        Log.e("EventViewModel", "Error fetching event details: ${e.message}")
                        // Handle the error as needed
                    }
                }
            } ?: Log.e("EventViewModel", "EventId is null or undefined")
        }
    }
}
