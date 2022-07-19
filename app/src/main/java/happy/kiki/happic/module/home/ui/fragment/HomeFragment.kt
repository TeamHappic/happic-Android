import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import happy.kiki.happic.databinding.FragmentHomeBinding
import happy.kiki.happic.module.core.util.AutoCleardValue

class HomeFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentHomeBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentHomeBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showButton(false)
    }

    fun showButton(isShow:Boolean){
        if (isShow) binding.btnUpload.visibility = View.VISIBLE
        else binding.btnUpload.visibility = View.INVISIBLE
    }
}