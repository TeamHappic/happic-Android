package happy.kiki.happic.module.report.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout.Tab
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentReportDetailBinding
import happy.kiki.happic.databinding.ItemReportCategoryBinding
import happy.kiki.happic.databinding.ItemReportYourKeywordBinding
import happy.kiki.happic.module.core.ui.widget.util.OnTabSelectedListenerAdapter
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.argument
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.loadUrlAsync
import happy.kiki.happic.module.core.util.setCornerSize
import happy.kiki.happic.module.core.util.yearMonthText
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.hour
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.parcelize.Parcelize

@Suppress("UNCHECKED_CAST")
class ReportDetailFragment : Fragment() {
    @Parcelize
    data class Argument(val tabIndex: Int, val category: ReportCategoryOption = hour) : Parcelable

    private val arg by argument<Argument>()

    private var binding by AutoCleardValue<FragmentReportDetailBinding>()
    private val reportVm by viewModels<ReportViewModel>({ requireParentFragment() })
    private val vm by viewModels<ReportDetailViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ReportDetailViewModel(arg.tabIndex) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReportDetailBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureHeader()
        configureMonthSelect()
        configureTabs()
        configureCategoryTabLayout()
        observeAndCallApi()
        bindingDatas()
    }

    private fun configureHeader() = binding.header.apply {
        setOnClickListener {
            reportVm.isMonthSelectOpened.value = !reportVm.isMonthSelectOpened.value
        }
        collectFlowWhenStarted(reportVm.isMonthSelectOpened.drop(1)) { isOpen ->
            if (isOpen) binding.monthSelect.fadeIn()
            else binding.monthSelect.fadeOut()

            binding.headerChevron.animate().rotation(if (isOpen) 0f else 180f).start()
        }
    }

    private fun configureMonthSelect() = binding.monthSelect.let { monthSelect ->
        collectFlowWhenStarted(reportVm.currentYear) {
            monthSelect.setCurrentYear(it)
        }
        collectFlowWhenStarted(reportVm.selectedYearMonth) { (year, month) ->
            binding.yearMonth.text = yearMonthText(year, month)
            monthSelect.setSelectedYearMonth(year, month)
        }
        monthSelect.onSelectedCurrentYear = {
            reportVm.currentYear.value = it
        }
        monthSelect.onSelectedYearMonth = { year, month ->
            reportVm.selectedYearMonth.value = year to month
            reportVm.isMonthSelectOpened.value = false
        }
    }

    private fun configureTabs() {
        collectFlowWhenStarted(vm.tabIndex) {
            listOf(binding.tabKeyword, binding.tabCategory, binding.tabMonthly).forEachIndexed { i, button ->
                button.isSelected = i == it
                button.setTextAppearance(if (i == it) R.style.C1_P_B12 else R.style.C2_P_M12)
            }
        }
        binding.tabKeyword.setOnClickListener { vm.tabIndex.value = 0 }
        binding.tabCategory.setOnClickListener { vm.tabIndex.value = 1 }
        binding.tabMonthly.setOnClickListener { vm.tabIndex.value = 2 }

        collectFlowWhenStarted(vm.tabIndex) {
            binding.keywordContainer.scheduleLayoutAnimation()
            binding.categoryContainer.scheduleLayoutAnimation()
            binding.monthlyContainer.scheduleLayoutAnimation()

            binding.keywordContainer.isVisible = it == 0
            binding.categoryContainer.isVisible = it == 1
            binding.monthlyContainer.isVisible = it == 2

            binding.sectionText.text = when (it) {
                0 -> "당신의 행복 키워드 순위"
                1 -> "카테고리 별 행복 키워드 순위"
                else -> "월 별 해픽 기록"
            }
        }
    }

    private fun configureCategoryTabLayout() {
        binding.sectionCategoryTabLayout.addOnTabSelectedListener(object : OnTabSelectedListenerAdapter() {
            override fun onTabSelected(tab: Tab) {
                reportVm.selectedCategoryTab.value = ReportCategoryOption.values()[tab.position]
            }
        })
        collectFlowWhenStarted(reportVm.selectedCategoryTab) {
            binding.sectionCategoryTabLayout.getTabAt(it.index)?.select()
        }
    }

    private fun observeAndCallApi() {
        collectFlowWhenStarted(reportVm.selectedYearMonth.combine(reportVm.selectedCategoryTab) { (year, month), category ->
            Triple(year, month, category)
        }) {
            vm.callApi(it.first, it.second, it.third)
        }
    }

    private fun bindingDatas() {
        collectFlowWhenStarted(vm.keywordApi.data) { data ->
            if (data != null) {
                binding.keywordContainer.removeAllViews()
                data.mapIndexed { index, it ->
                    ItemReportYourKeywordBinding.inflate(layoutInflater, binding.keywordContainer, false).apply {
                        rank.text = (index + 1).toString()
                        keyword.text = it.content
                        category.text = "#${it.category.name}"
                        count.text = "${it.count}회"
                    }
                }.forEach {
                    binding.keywordContainer.addView(it.root, MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                        bottomMargin = requireContext().px(8)
                    })
                }
            }
        }

        collectFlowWhenStarted(vm.categoryApi.data) { data ->
            if (data != null) {
                binding.categoryList.removeAllViews()
                data.map {
                    ItemReportCategoryBinding.inflate(
                        layoutInflater, binding.categoryList, false
                    ) to it
                }.forEachIndexed { index, (itemBinding, item) ->
                    itemBinding.category.text = "#${reportVm.selectedCategoryTab.value.name}"
                    itemBinding.rank.text = (index + 1).toString()
                    itemBinding.keyword.text = item.content
                    itemBinding.count.text = "${item.count}회"

                    itemBinding.imageContainer.removeAllViews()
                    itemBinding.imageContainer.isVisible = item.images.isNotEmpty()
                    item.images.map { image ->
                        ShapeableImageView(context).apply {
                            val imageSize = (screenWidth - px(96)) / 3
                            layoutParams = LinearLayout.LayoutParams(imageSize, imageSize, 1f)
                            loadUrlAsync(image)
                            setCornerSize(8)
                        }
                    }.forEach {
                        itemBinding.imageContainer.addView(it)
                    }
                    binding.categoryList.addView(itemBinding.root.apply {
                        updateLayoutParams<MarginLayoutParams> {
                            bottomMargin = px(8)
                        }
                    })
                }
            }
        }


        collectFlowWhenStarted(vm.monthlyApi.data) { data ->
            if(data != null) {
                binding.monthlyRecordPlanet.month = data.month
                binding.monthlyRecordPlanet.count = data.count
            }
        }
    }
}