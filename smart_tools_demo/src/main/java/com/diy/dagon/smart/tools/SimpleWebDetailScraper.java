package com.diy.dagon.smart.tools;

/**
 * @Description TODO
 * @Date 2024/10/29 12:09
 * @Author by liyu-jk
 */
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class SimpleWebDetailScraper {
    public static void main(String[] args) {
        String url = "https://www.amazon.com/SOLPERK-Maintainer-Waterproof-Controller-Adjustable/dp/B08GX19KT9/ref=zg_bs_g_2236628011_d_sccl_1/138-6926419-1611852?psc=1";

        try {
            //获取商品详情
            Connection connection = Jsoup.connect(url);
            connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36");
            Document detailDoc = connection.get();

            //选择产品描述
            Elements selectDetailLi = detailDoc.select("#featurebullets_feature_div");
            for (Element element : selectDetailLi) {
                String txtDetail = element.select("span").select(".a-list-item").text();
                System.err.println(txtDetail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

