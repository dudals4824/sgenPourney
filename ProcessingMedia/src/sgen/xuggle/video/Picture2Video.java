package sgen.xuggle.video;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;

public class Picture2Video {

	private static final double FRAME_RATE = 0.25;
	private static final int SECONDS_TO_RUN_FOR = 16;
	private static final String outputFilename = "C:/image/new3.mp4";
	private static Dimension screenBounds;
	int audioStreamIndex = 0;
	int audioStreamId = 0;
	int channelCount = 2;
	int sampleRate = 44100;
	int totalSeconds = 10;

	public static void main(String[] args) {
		int i = 0;
		// let's make a IMediaWriter to write the file.
		final IMediaWriter writer = ToolFactory.makeWriter(outputFilename);
		screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
		// writer.addListener(ToolFactory.makeViewer(
		// IMediaViewer.Mode.AUDIO_VIDEO, true,
		// javax.swing.WindowConstants.EXIT_ON_CLOSE));
		// We tell it we're going to add one video stream, with id 0,
		// at position 0, and that it will have a fixed frame rate of
		// FRAME_RATE.
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,
				screenBounds.width / 4, screenBounds.height / 4);
		// ICodec codec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_MP3);
		IContainer container = writer.getContainer();
		// int streamIndex = writer.addAudioStream(
		// 0, 0, codec, 2, 44110);
		// IStream stream = container.getStream(streamIndex);
		// int sampleCount = stream.getStreamCoder().getDefaultAudioFrameSize();

		// writer.addAudioStream(audioStreamIndex, audioStreamId,
		// channelCount, sampleRate);

		long startTime = System.nanoTime();

		File[] seqimg = new File[4];

		for (i = 0; i < 4; i++)
			seqimg[i] = new File("C:/image/" + (i + 1) + ".png");

		// short[] javaSamples = new short[sampleCount*2];
		for (int index = 0; index < SECONDS_TO_RUN_FOR * FRAME_RATE; index++) {

			// take the screen shot

			BufferedImage screen = (getImage(seqimg[index]));
			BufferedImage bgrScreen = convertToType(screen,
					BufferedImage.TYPE_3BYTE_BGR);
			// convert to the right image type
			// encode the image to stream #0
			writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime,
					TimeUnit.NANOSECONDS);
			// writer.encodeAudio(streamIndex, javaSamples);
			// writer.addVideoStream(arg0, arg1, arg2, arg3, arg4)
			// writer.addAudioStream(arg0, arg1, arg2, arg3);
			// sleep for frame rate milliseconds
			try {
				Thread.sleep((long) (1000 / FRAME_RATE));
			} catch (InterruptedException e) {
				// ignore
			}

		}

		// tell the writer to close and write the trailer if needed
		writer.close();

	}

	public static BufferedImage convertToType(BufferedImage sourceImage,
			int targetType) {

		BufferedImage image;

		// if the source image is already the target type, return the source
		// image
		if (sourceImage.getType() == targetType) {
			image = sourceImage;
		}
		// otherwise create a new image of the target type and draw the new
		// image
		else {
			image = new BufferedImage(sourceImage.getWidth(),
					sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}

		return image;

	}

	private static BufferedImage getImage(File file) {
		try {
			BufferedImage image = ImageIO.read(file);
			return image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private IVideoPicture getRGBPicture(IVideoPicture picture) {
		IVideoResampler resampler = null;
		if (picture.getPixelType() != IPixelFormat.Type.BGR24) {
			// if this stream is not in BGR24, we're going to need to
			// convert it. The VideoResampler does that for us.
			resampler = IVideoResampler.make(picture.getWidth(),
					picture.getHeight(), IPixelFormat.Type.BGR24,
					picture.getWidth(), picture.getHeight(),
					picture.getPixelType());
			if (resampler == null)
				throw new RuntimeException(
						"could not create color space resampler for picture");
		}
		if (resampler != null) {
			// we must resample
			IVideoPicture newPic = IVideoPicture.make(
					resampler.getOutputPixelFormat(), picture.getWidth(),
					picture.getHeight());
			if (resampler.resample(newPic, picture) < 0)
				throw new RuntimeException("could not resample video ");

			picture = newPic;
		}
		return picture;
	}

}