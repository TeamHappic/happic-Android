import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentDailyHappicPhotoBinding
import happy.kiki.happic.databinding.ItemDailyHappicPhotoBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.debugE
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyHappicPhotoFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicPhotoBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicPhotoBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        setOnClickListener()
        setCards()
    }

    private fun initData() {
        with(binding) {
            val now = LocalDate.now()
            tvMonth.text = now.format(DateTimeFormatter.ofPattern("yyyy.MM")).toString()
            tvYear.text = now.year.toString()
        }
    }

    private fun setOnClickListener() {
        binding.borderMonth.setOnClickListener {
            if (binding.borderSelect.visibility == View.VISIBLE) binding.borderSelect.visibility = View.GONE
            else binding.borderSelect.visibility = View.VISIBLE
        }
    }

    private fun setCards() {
        (0..30).map {
            ItemDailyHappicPhotoBinding.inflate(layoutInflater).apply {
                root.id = ViewCompat.generateViewId()
            }
        }.forEach {
            binding.clCards.addView(it.root)
            binding.flowCards.referencedIds
        }

    }
}