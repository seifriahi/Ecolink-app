import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ecolink.R
import com.ecolink.brand.DetailsActivity
import com.ecolink.brand.ProductDetailsActivity
import com.ecolink.fragments.NewsFragment
import com.ecolink.models.News

class NewsAdapter( var newsList: MutableList<News>) :
    RecyclerView.Adapter<NewsAdapter.NewsListViewHolder>() {

    class NewsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val newsDescription: TextView = itemView.findViewById(R.id.newsDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_item_news, parent, false)
        return NewsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val ti = newsList[position].title
        val desc = newsList[position].description
        val imagelink = newsList[position].imageRes

        holder.newsImage.load("https://ecolink.onrender.com/images/$imagelink")
        holder.newsTitle.text = ti
        holder.newsDescription.text = desc

        holder.itemView.setOnClickListener {
            val fragmentManager = (holder.itemView.context as FragmentActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val detailsFragment = DetailsFragment1.newInstance(imagelink, ti, desc)

            // Replace the current fragment with DetailsFragment
            fragmentTransaction.replace(R.id.fragment_container, detailsFragment)

            // Optional: Add the transaction to the back stack for back navigation
            fragmentTransaction.addToBackStack(null)

            // Commit the transaction
            fragmentTransaction.commit()
        }
    }
    override fun getItemCount() = newsList.size


}