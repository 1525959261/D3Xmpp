package com.d3.d3xmpp.constant;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.d3.d3xmpp.model.User;
import com.d3.d3xmpp.util.ImgHandler;
import com.d3.d3xmpp.xmpp.XmppConnection;
import com.d3.d3xmpp.R;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImgConfig extends ImageLoader {
	private static DisplayImageOptions options_circle;
	private static AnimateFirstDisplayListener animateFirstDisplayListener = new AnimateFirstDisplayListener();

	/**
	 * @param url
	 *            ���������ļ���
	 * @param imageView
	 *            Բ�� R for Round
	 */
	public static void showImg(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url,
				imageView, options_circle, animateFirstDisplayListener);
	}
	
	public static void showHeadImg(String username, ImageView imageView) {
//		ImageLoader.getInstance().displayImage("http://121.52.216.138:9090/plugins/xinxy/apps/userinfo/getuserheadimagetojpg?userName="+username,
//				imageView, options_circle, animateFirstDisplayListener);
		imageView.setImageDrawable(ImgHandler.ToCircularBig(R.drawable.default_icon));
		User user = new User(XmppConnection.getInstance().getUserInfo(username));
		user.showHead(imageView);
	}
	

	/**
	 * ��ʼ��ͼƬ��ȡ��ʽ
	 */
	public static void initImageLoader() {

		options_circle = new DisplayImageOptions.Builder()
				.showImageOnLoading(
						ImgHandler.ToCircularBig(R.drawable.default_icon))
				.showImageForEmptyUri(
						ImgHandler.ToCircularBig(R.drawable.default_icon))
				.showImageOnFail(
						ImgHandler.ToCircularBig(R.drawable.default_icon))
				.cacheInMemory(true).cacheOnDisc(false).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(1000))
				// ��δ�о���Բ�Σ���ʹ��һ����ֵ
				.resetViewBeforeLoading(false)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.build();

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				MyApplication.getInstance()).threadPriority(Thread.NORM_PRIORITY)
//				.denyCacheImageMultipleSizesInMemory() // ��ͬ��СͼƬֻ��һ�����棬Ĭ�϶��
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// ����ͼƬ���غ���ʾ�Ĺ�����������
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * @author Administrator ������ȡ��ͼƬ
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {
		// �ŵ��ڴ�
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
