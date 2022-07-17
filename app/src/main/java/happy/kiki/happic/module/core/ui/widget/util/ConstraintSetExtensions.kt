package happy.kiki.happic.module.core.ui.widget.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.TOP

fun ConstraintSet.topToTop(v1: View, v2: View) {
    connect(v1.id, TOP, v2.id, TOP)
}

fun ConstraintSet.topToTop(v1: View) {
    connect(v1.id, TOP, PARENT_ID, TOP)
}

fun ConstraintSet.bottomToBottom(v1: View, v2: View) {
    connect(v1.id, BOTTOM, v2.id, BOTTOM)
}

fun ConstraintSet.bottomToBottom(v1: View) {
    connect(v1.id, BOTTOM, PARENT_ID, BOTTOM)
}

fun ConstraintSet.topToBottom(v1: View, v2: View) {
    connect(v1.id, TOP, v2.id, BOTTOM)
}

fun ConstraintSet.topToBottom(v1: View) {
    connect(v1.id, TOP, PARENT_ID, BOTTOM)
}

fun ConstraintSet.bottomToTop(v1: View, v2: View) {
    connect(v1.id, BOTTOM, v2.id, TOP)
}

fun ConstraintSet.bottomToTop(v1: View) {
    connect(v1.id, BOTTOM, PARENT_ID, TOP)
}