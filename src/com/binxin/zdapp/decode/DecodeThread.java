package com.binxin.zdapp.decode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.binxin.zdapp.activity.App_decode;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * This thread does all the heavy lifting of decoding the images.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
final class DecodeThread extends Thread {

	public static final String BARCODE_BITMAP = "barcode_bitmap";
	public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

	private final App_decode activity;
	private final Map<DecodeHintType, Object> hints;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(App_decode activity,
			Collection<BarcodeFormat> decodeFormats,
			Map<DecodeHintType, ?> baseHints, String characterSet,
			ResultPointCallback resultPointCallback) {

		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);

		hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		if (baseHints != null) {
			hints.putAll(baseHints);
		}

		// The prefs can't change while the thread is running, so pick them up
		// once here.
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			// SharedPreferences prefs = PreferenceManager
			// .getDefaultSharedPreferences(activity);
			decodeFormats = EnumSet.noneOf(BarcodeFormat.class);

			/* 配置修改 */
			// if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_1D, false)) {
			// decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			// }
			// if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_QR, false)) {
			// decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			// }
			// if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_DATA_MATRIX,
			// false)) {
			// decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
			// }

			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);//支持一维条码
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}
		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
		Log.i("DecodeThread", "Hints: " + hints);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity, hints);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
