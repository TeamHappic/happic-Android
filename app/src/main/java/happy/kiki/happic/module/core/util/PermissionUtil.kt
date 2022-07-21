package happy.kiki.happic.module.core.util

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/**
 * Dexter를 이용한 권한 요청 및 콜백에 관련된 유틸
 * @author MJStudio
 * @see android.Manifest.permission
 */
class PermissionDexterUtil {
    /**
     * 권한들이 허용되었는지 검사를 요청하는 함수
     *
     * @param activity Dexter 라이브러리가 사용할 Activity 객체
     * @param listener 권한들에 대한 검사가 완료되었을 때 콜백을 처리할 리스너
     * @param permissions 요청하는 권한 목록 [android.Manifest.permission]
     */
    fun requestPermissions(activity: Activity, listener: PermissionListener, permissions: Collection<String>) {

        val callbackListener: MultiplePermissionsListener = object : BaseMultiplePermissionsListener() {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                val deniedPermissions = report.deniedPermissionResponses.map { it.permissionName }
                val permanentlyDeniedPermissions =
                    report.deniedPermissionResponses.filter { it.isPermanentlyDenied }.map { it.permissionName }

                // 모든 권한이 허가되었다면,
                if (report.areAllPermissionsGranted()) {
                    listener.onPermissionGranted()
                } // 권한 중에 영구적으로 거부된 권한이 있다면
                else if (report.isAnyPermissionPermanentlyDenied) {
                    listener.onAnyPermissionsPermanentlyDeined(deniedPermissions, permanentlyDeniedPermissions)
                } // 권한 중에 거부된 권한이 있다면
                else {
                    listener.onPermissionShouldBeGranted(deniedPermissions)
                }
            }

        }

        /**
         * Dexter로 activity를 이용한 권한 요청
         */
        Dexter.withActivity(activity).withPermissions(permissions).withListener(callbackListener).check()
    }

    interface PermissionListener {
        /**
         * 모든 권한이 허용되었다.
         */
        fun onPermissionGranted() {}

        /**
         * 일부 권한이 거부되었다.
         *
         * @param deniedPermissions 거부된 권한 목록
         */
        fun onPermissionShouldBeGranted(deniedPermissions: List<String>) {}

        /**
         * 일부 권한이 영구적으로 거부되었다.
         *
         * @param deniedPermissions 거부된 권한 목록
         * @param permanentDeniedPermissions 영구적으로 거부된 권한 목록
         */
        fun onAnyPermissionsPermanentlyDeined(
            deniedPermissions: List<String>, permanentDeniedPermissions: List<String>
        ) {
        }
    }
}