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
package cn.com.xuxiaowei.servlet;

import cn.com.xuxiaowei.entity.QrCode;
import cn.com.xuxiaowei.service.QrCodeService;
import cn.com.xuxiaowei.zxing.QrCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 授权登录 二维码生成器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Component
public class QrCodeHttpServlet extends HttpServlet {

    private QrCodeService qrCodeService;

    @Autowired
    public void setQrCodeService(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        StringBuffer requestUrl = req.getRequestURL();
        int requestUrlLength = requestUrl.length();

        requestUrl = requestUrl.replace(requestUrlLength - req.getRequestURI().length(), requestUrlLength,
                "/oauth/authorize/qr/confirm");

        String uuid = req.getParameter("uuid");
        if (uuid == null || "".equals(uuid)) {
            // 处理请求 uuid 不合法

        } else {

            // 有效期判断（过期删除）
            QrCode byUuidCacheable = qrCodeService.getByUuidCacheable(uuid);

            if (byUuidCacheable != null) {

                requestUrl.append("?uuid=").append(uuid);

                qrcode(resp, requestUrl.toString());
            } else {

            }
        }

    }

    /**
     * 二维码生成
     */
    private void qrcode(HttpServletResponse response, String url) throws IOException {
        QrCodeWriter qrCodeWriter = new QrCodeWriter();

        // 设置二维码静态区域大小
        qrCodeWriter.setQuietZoneSize(2);

        try {

            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 470, 470);

            ByteArrayOutputStream imageOut = new ByteArrayOutputStream(1024);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", imageOut);

            byte[] imageData = imageOut.toByteArray();

            response.setContentType("image/png");
            response.setContentLength(imageData.length);
            response.setHeader("Cache-Control", "public");
            response.getOutputStream().write(imageData);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
