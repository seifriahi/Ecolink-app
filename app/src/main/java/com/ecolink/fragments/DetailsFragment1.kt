import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.ecolink.R

class DetailsFragment1 : Fragment() {

    private var imagelink: String? = null
    private var productTitle: String? = null
    private var productDescription: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagelink = it.getString("imagelink")
            productTitle = it.getString("producttitle")
            productDescription = it.getString("productDescription")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details1, container, false)

        // Find your UI elements in the view and update them
        val imageView: ImageView = view.findViewById(R.id.ivDetails)
        val titleTextView: TextView = view.findViewById(R.id.tvDetailsProductName)
        val descriptionTextView: TextView = view.findViewById(R.id.tvDetailsProductDescription)

        // Load image using Coil library
        imageView.load("https://ecolink.onrender.com/images/$imagelink")

        // Update text views
        titleTextView.text = productTitle
        descriptionTextView.text = productDescription

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(imagelink: String, productTitle: String, productDescription: String) =
            DetailsFragment1().apply {
                arguments = Bundle().apply {
                    putString("imagelink", imagelink)
                    putString("producttitle", productTitle)
                    putString("productDescription", productDescription)
                }
            }
    }
}
