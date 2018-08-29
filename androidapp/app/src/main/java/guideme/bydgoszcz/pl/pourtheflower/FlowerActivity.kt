package guideme.bydgoszcz.pl.pourtheflower

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import guideme.bydgoszcz.pl.pourtheflower.dummy.FlowersContent
import kotlinx.android.synthetic.main.activity_flower.*
import kotlinx.android.synthetic.main.content_flower.*

class FlowerActivity : AppCompatActivity() {
    private var item: FlowersContent.FlowerItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flower)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        item = intent.getSerializableExtra("Flower") as FlowersContent.FlowerItem?
        val flower = item
        if (flower != null) {
            toolbar_layout.title = flower.content
            flowerDescription.text = flower.description
        }
        toolbar_imageView.setOnClickListener {
            if (flower == null) {
                return@setOnClickListener;
            }
            val transaction = supportFragmentManager.beginTransaction()
            val dialog = supportFragmentManager.findFragmentByTag("dialog")
            if (dialog != null) {
                transaction.remove(dialog)
            }

            transaction.addToBackStack(null)

            val imageDialog = ImageDialog()
            imageDialog.arguments = Bundle().apply {
                putString(imageDialog.IMAGE_URL, flower.imageUrl)
            }
            imageDialog.show(transaction, "dialog")
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        val flower = item
        if (flower != null) {
            val parentView = toolbar_imageView.parent as ViewGroup
            parentView.post {
                if (flower.imageUrl.isEmpty()) {
                    return@post
                }
                Picasso.get().load(flower.imageUrl)
                        .resize(parentView.measuredWidth, parentView.measuredHeight)
                        .centerInside()
                        .into(toolbar_imageView)
            }
        }
    }
}
