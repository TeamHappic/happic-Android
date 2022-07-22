package happy.kiki.happic.module.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout.Tab
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentReportBinding
import happy.kiki.happic.databinding.ItemReportCategoryBinding
import happy.kiki.happic.databinding.ItemReportYourKeywordBinding
import happy.kiki.happic.module.core.ui.widget.util.OnTabSelectedListenerAdapter
import happy.kiki.happic.module.core.util.AutoClearedValue
import happy.kiki.happic.module.core.util.emitEvent
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.yearMonthText
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.hour
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.what
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.where
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.who
import happy.kiki.happic.module.report.ui.fragment.ReportDetailFragment.Argument
import happy.kiki.happic.module.report.ui.widget.ReportRoundImageView
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop

class ReportFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentReportBinding>()
    private val vm by viewModels<ReportViewModel>({ requireParentFragment() })
    private val navigationVm by viewModels<ReportNavigationViewModel>({ requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReportBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = vm
        configureHeader()
        configureMonthSelect()
        configureUI()
    }

    private fun configureHeader() = binding.header.apply {
        setOnClickListener {
            vm.isMonthSelectOpened.value = !vm.isMonthSelectOpened.value
        }
        collectFlowWhenStarted(vm.isMonthSelectOpened.drop(1)) { isOpen ->
            if (isOpen) binding.monthSelect.fadeIn()
            else binding.monthSelect.fadeOut()

            binding.headerChevron.animate().rotation(if (isOpen) 0f else 180f).start()
        }
    }

    private fun configureMonthSelect() = binding.monthSelect.let { monthSelect ->
        collectFlowWhenStarted(vm.currentYear) {
            monthSelect.setCurrentYear(it)
        }
        collectFlowWhenStarted(vm.selectedYearMonth) { (year, month) ->
            binding.yearMonth.text = yearMonthText(year, month)
            monthSelect.setSelectedYearMonth(year, month)
        }
        monthSelect.onSelectedCurrentYear = {
            vm.currentYear.value = it
        }
        monthSelect.onSelectedYearMonth = { year, month ->
            vm.selectedYearMonth.value = year to month
            vm.isMonthSelectOpened.value = false
        }
    }

    private fun configureUI() {
        configureMomentSection()
        configureKeywordSection()
        configureCategorySection()
        configureMonthlyRecordSection()
    }

    private fun configureMomentSection() {
        binding.sectionMomentText.text = buildSpannedString {
            append("이번 달 베스트")
            color(getColor(R.color.orange)) {
                append(" 해픽 ")
            }
            append("모멘트는 이거야!")
        }

        binding.momentWhatSuffix.text = buildSpannedString {
            append("하는 순간 ")
            bold {
                append("가장 ")
                color(getColor(R.color.orange)) {
                    append("행복")
                }
                append("했어요")
            }
        }

        collectFlowWhenStarted(vm.reportHomeApi.isLoading) {
            binding.momentIndicator.isVisible = it
            binding.momentDataContainer.isVisible = !it
        }

        collectFlowWhenStarted(vm.reportHomeApi.data) {
            it?.let { data ->
                binding.momentDataContainer.isVisible = data.rank1s.size == 4
                binding.momentEmpty.isVisible = data.rank1s.size != 4
                if (data.rank1s.size == 4) {
                    binding.momentWhen.text = data.rank1s[0].content
                    binding.momentWhere.text = data.rank1s[1].content
                    binding.momentWho.text = data.rank1s[2].content
                    binding.momentWhat.text = data.rank1s[3].content
                }
            }
        }
    }

    private fun configureKeywordSection() {
        binding.sectionKeyword.setOnClickListener {
            emitEvent(navigationVm.onNavigateDetail, Argument(0))
        }

        collectFlowWhenStarted(vm.reportHomeApi.data) {
            it?.let { data ->
                binding.keywordRankContainer.removeAllViews()
                binding.emptyKeyword.isVisible = data.rank2s.isEmpty()
                data.rank2s.map {
                    ItemReportYourKeywordBinding.inflate(
                        layoutInflater, binding.keywordRankContainer, false
                    ) to it
                }.forEachIndexed { index, (itemBinding, item) ->
                    itemBinding.rank.text = (index + 1).toString()
                    itemBinding.keyword.text = item.content
                    itemBinding.category.text = "#${item.category.name}"
                    itemBinding.count.text = "${item.count}회"

                    binding.keywordRankContainer.addView(itemBinding.root.apply {
                        updateLayoutParams<MarginLayoutParams> {
                            topMargin = px(if (index == 0) 0 else 8)
                        }
                    })
                }
            }
        }
    }

    private fun configureCategorySection() {
        binding.sectionCategory.setOnClickListener {
            emitEvent(navigationVm.onNavigateDetail, Argument(1, vm.selectedCategoryTab.value))
        }
        binding.sectionCategoryTabLayout.addOnTabSelectedListener(object : OnTabSelectedListenerAdapter() {
            override fun onTabSelected(tab: Tab) {
                vm.selectedCategoryTab.value = ReportCategoryOption.values()[tab.position]
            }
        })
        collectFlowWhenStarted(vm.selectedCategoryTab) {
            binding.sectionCategoryTabLayout.getTabAt(it.index)?.select()
        }
        collectFlowWhenStarted(vm.reportHomeApi.data.combine(vm.selectedCategoryTab, ::Pair)) { (data, category) ->
            data?.run {
                binding.categoryRankContainer.removeAllViews()

                val contents = when (category) {
                    hour -> data.rank3s.whenX
                    where -> data.rank3s.where
                    who -> data.rank3s.who
                    what -> data.rank3s.what
                }

                binding.emptyCategory.isVisible = contents.isEmpty()

                contents.map {
                    ItemReportCategoryBinding.inflate(
                        layoutInflater, binding.categoryRankContainer, false
                    ) to it
                }.forEachIndexed { index, (itemBinding, item) ->
                    itemBinding.category.text = "#${vm.selectedCategoryTab.value.name}"
                    itemBinding.rank.text = (index + 1).toString()
                    itemBinding.keyword.text = item.content
                    itemBinding.count.text = "${item.count}회"

                    itemBinding.imageContainer.removeAllViews()
                    itemBinding.imageContainer.isVisible = item.images.isNotEmpty()

                    if (item.images.isNotEmpty()) {
                        val dummyImage: (Int) -> String = { "dummy" }

                        val filledCount = 3 - item.images.size
                        (item.images.take(3) + List(filledCount, dummyImage)).forEachIndexed { idx, image ->
                            itemBinding.imageContainer.addView(ReportRoundImageView(requireContext()).apply {
                                bind(image, (screenWidth - px(96)) / 3, if (idx == 0) 0 else px(12))
                            })
                        }
                    }

                    binding.categoryRankContainer.addView(itemBinding.root.apply {
                        updateLayoutParams<MarginLayoutParams> {
                            topMargin = px(if (index == 0) 0 else 8)
                        }
                    })
                }
            }
        }
    }

    private fun configureMonthlyRecordSection() {
        binding.sectionMonthly.setOnClickListener {
            emitEvent(navigationVm.onNavigateDetail, Argument(2))
        }
        collectFlowWhenStarted(vm.reportHomeApi.data) {
            it?.run {
                binding.monthlyRecordPlanet.month = this.rank4s.month
                binding.monthlyRecordPlanet.count = this.rank4s.count
            }
        }
    }
}