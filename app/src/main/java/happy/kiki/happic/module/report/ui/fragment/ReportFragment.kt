package happy.kiki.happic.module.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout.Tab
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentReportBinding
import happy.kiki.happic.databinding.ItemReportCategoryBinding
import happy.kiki.happic.databinding.ItemReportYourKeywordBinding
import happy.kiki.happic.module.core.ui.widget.util.OnTabSelectedListenerAdapter
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.loadUrlAsync
import happy.kiki.happic.module.core.util.setCornerSize
import happy.kiki.happic.module.core.util.yearMonthText
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import kotlinx.coroutines.flow.drop

class ReportFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentReportBinding>()
    private val viewModel by viewModels<ReportViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReportBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        configureHeader()
        configureMonthSelect()
        configureUI()
    }

    private fun configureHeader() = binding.header.apply {
        setOnClickListener {
            viewModel.isMonthSelectOpened.value = !viewModel.isMonthSelectOpened.value
        }
        collectFlowWhenStarted(viewModel.isMonthSelectOpened.drop(1)) { isOpen ->
            if (isOpen) binding.monthSelect.fadeIn()
            else binding.monthSelect.fadeOut()

            binding.headerChevron.animate().rotation(if (isOpen) 0f else 180f).start()
        }
    }

    private fun configureMonthSelect() = binding.monthSelect.let { monthSelect ->
        collectFlowWhenStarted(viewModel.currentYear) {
            monthSelect.setCurrentYear(it)
        }
        collectFlowWhenStarted(viewModel.selectedYearMonth) { (year, month) ->
            binding.yearMonth.text = yearMonthText(year, month)
            monthSelect.setSelectedYearMonth(year, month)
        }
        monthSelect.onSelectedCurrentYear = {
            viewModel.currentYear.value = it
        }
        monthSelect.onSelectedYearMonth = { year, month ->
            viewModel.selectedYearMonth.value = year to month
            viewModel.isMonthSelectOpened.value = false
        }
    }

    private fun configureUI() {
        configureMomentSection()
        configureKeywordSection()
        configureCategorySection()
    }

    private fun configureMomentSection() {
        binding.sectionMomentText.text = buildSpannedString {
            append("이번 달 베스트")
            color(getColor(R.color.orange)) {
                append(" 해픽 ")
            }
            append("모멘트는 이거야!")
        }
    }

    private fun configureKeywordSection() {
        binding.sectionKeyword.setOnClickListener {

        }
        collectFlowWhenStarted(viewModel.reportHomeApi.data) {
            it?.run {
                rank2s.map {
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
            } ?: kotlin.run {
                binding.keywordRankContainer.removeAllViews()
            }
        }
    }

    private fun configureCategorySection() {
        binding.sectionCategoryTabLayout.addOnTabSelectedListener(object : OnTabSelectedListenerAdapter() {
            override fun onTabSelected(tab: Tab) {
                viewModel.selectedCategoryTab.value = ReportCategoryOption.values()[tab.position]
            }
        })
        collectFlowWhenStarted(viewModel.selectedCategoryTab) {
            binding.sectionCategoryTabLayout.getTabAt(it.index)?.select()
        }
        collectFlowWhenStarted(viewModel.reportHomeApi.data) {
            it?.run {
                rank3s.map {
                    ItemReportCategoryBinding.inflate(
                        layoutInflater, binding.categoryRankContainer, false
                    ) to it
                }.forEachIndexed { index, (itemBinding, item) ->
                    itemBinding.category.text = "#${viewModel.selectedCategoryTab.value.name}"
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

                    binding.categoryRankContainer.addView(itemBinding.root.apply {
                        updateLayoutParams<MarginLayoutParams> {
                            topMargin = px(if (index == 0) 0 else 8)
                        }
                    })
                }
            } ?: kotlin.run {
                binding.keywordRankContainer.removeAllViews()
            }
        }
    }
}