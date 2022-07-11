import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import happy.kiki.happic.databinding.FragmentDailyHappicPhotoBinding
import happy.kiki.happic.databinding.ItemDailyHappicPhotoBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.fadeIn
import happy.kiki.happic.module.core.util.fadeOut
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
            if (binding.borderSelect.isVisible) binding.borderSelect.fadeOut()
            else binding.borderSelect.fadeIn()
        }
    }

    private fun setCards() {
        (0..30).map {
            ItemDailyHappicPhotoBinding.inflate(layoutInflater).apply {
                root.id = ViewCompat.generateViewId()
            }
        }.forEach { itemBinding ->
            val width = (requireContext().screenWidth - requireContext().px(55)) / 4
            binding.clCards.addView(itemBinding.root, ConstraintLayout.LayoutParams(width, WRAP_CONTENT))
            binding.flowCards.addView(itemBinding.root)
        }

    }
}