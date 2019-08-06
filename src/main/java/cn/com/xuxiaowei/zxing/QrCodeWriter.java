/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.xuxiaowei.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Map;

/**
 * 自定义谷歌二维码
 * <p>
 * 此对象将QR码呈现为灰度值的BitMatrix 2D阵列。
 * <p>
 * 增加自定义边界大小
 *
 * @author xuxiaowei
 * @see QRCodeWriter
 * @since 0.0.1
 */
public class QrCodeWriter implements Writer {

    /**
     * 静态区域大小
     * <p>
     * 单位：厘米
     */
    private int quietZoneSize = 4;

    /**
     * @param format 无论填写什么，都是{@link BarcodeFormat#QR_CODE}，
     *               二维码只支持{@link BarcodeFormat#QR_CODE}，其他报错
     * @see QRCodeWriter#encode(String, BarcodeFormat, int, int)
     */
    @Override
    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        return encode(contents, format, width, height, null);
    }

    /**
     * @param format 无论填写什么，都是{@link BarcodeFormat#QR_CODE}，
     *               二维码只支持{@link BarcodeFormat#QR_CODE}，其他报错
     * @see QRCodeWriter#encode(String, BarcodeFormat, int, int, Map)
     */
    @Override
    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }

        // 防止填错时报错
        // if (format != BarcodeFormat.QR_CODE) {
        // throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
        // }

        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' + height);
        }

        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int quietZone = getQuietZoneSize();
        if (hints != null) {
            if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (hints.containsKey(EncodeHintType.MARGIN)) {
                quietZone = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
            }
        }

        QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
        return renderResult(code, width, height, quietZone);
    }

    /**
     * 注意，输入矩阵使用0 ==白色，1 ==黑色，而输出矩阵使用0 ==黑色，255 ==白色（即8位灰度位图）。
     */
    private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);
        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);

        // 填充包括静区和额外的白色像素，以适应所请求的尺寸。
        // 例如，如果输入为25x25，则QR将为33x33，包括静区。
        // 如果请求的大小为200x160，则倍数为4，QR为132x132。
        // 这些将处理从100x100（实际QR）到200x160的所有填充。
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);

        for (int inputy = 0, outputy = topPadding; inputy < inputHeight; inputy++, outputy += multiple) {
            // 写下该行条形码的内容
            for (int inputx = 0, outputx = leftPadding; inputx < inputWidth; inputx++, outputx += multiple) {
                if (input.get(inputx, inputy) == 1) {
                    output.setRegion(outputx, outputy, multiple, multiple);
                }
            }
        }

        return output;
    }

    public void setQuietZoneSize(int quietZoneSize) {
        this.quietZoneSize = quietZoneSize;
    }

    public int getQuietZoneSize() {
        return quietZoneSize;
    }
}
