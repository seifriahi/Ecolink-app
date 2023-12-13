import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.load
import com.ecolink.R
import com.ecolink.api.Api
import com.ecolink.databinding.FragmentDetailBinding
import com.ecolink.fragments.CommentsFragment
import com.ecolink.models.Lesson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var selectedLesson: Lesson // Assuming Lesson is Parcelable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if selectedLesson is initialized before accessing its properties
        if (::selectedLesson.isInitialized) {
            // Update views with information from the selected lesson
            binding.image.load("https://ecolink.onrender.com/images/${selectedLesson.imageRes}")
            binding.title1.text = selectedLesson.title
            binding.txtAbout.text = selectedLesson.description

            // Set click listener for the "Delete" button


            // Set click listener for the "View Comments" button
            binding.btnViewComments.setOnClickListener {
                navigateToCommentsFragment()
            }
        } else {
            // Handle the case where selectedLesson is not initialized
            Log.e("DetailFragment", "selectedLesson is not initialized")
        }
    }

    // Add a function to set the selected lesson
    fun setSelectedLesson(lesson: Lesson) {
        this.selectedLesson = lesson
    }

    private fun deleteLesson(lessonId: String?) {
        // Use safe call operator to check for null or blank
        if (lessonId.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Lesson ID is null or empty",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val apiInterface = Api.create()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = apiInterface.deleteLesson(lessonId)
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Deleted successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to delete lesson",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Handle failure
                Log.e("DetailFragment", "Failed to delete lesson. ${e.message}")
            }
        }
    }

    private fun navigateToCommentsFragment() {
        Log.d("DetailFragment", "selectedLesson: $selectedLesson")
        Log.d("DetailFragment", "selectedLesson.id: ${selectedLesson.id}")

        if (::selectedLesson.isInitialized && !selectedLesson.id.isNullOrBlank()) {
            // Rest of the code...
        } else {
            Log.e("DetailFragment", "Selected lesson or its id is null or blank")
        }
    }

}
