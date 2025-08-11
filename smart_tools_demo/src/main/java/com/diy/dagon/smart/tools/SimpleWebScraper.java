package com.diy.dagon.smart.tools;

/**
 * @Description TODO
 * @Date 2024/10/29 12:09
 * @Author by liyu-jk
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SimpleWebScraper {
    public static void main(String[] args) {
        String url = "https://www.amazon.com/gp/bestsellers/lawn-garden/2236628011/ref=pd_zg_hrsr_lawn-garden";

        try {
            // 连接到网址
            Document doc = Jsoup.connect(url).get();

            // 选择产品的标题和描述
            Elements products = doc.select("#gridItemRoot");
            for (Element product : products) {
                //获取标题
                String title = product.select("._cDEzb_p13n-sc-css-line-clamp-3_g3dy1").text();
                System.out.println("产品标题: " + title);

                //获取商品详情
                String detailHref = "https://www.amazon.com" + product.select("a").attr("href");
                System.out.println(detailHref);
                Document detailDoc = Jsoup.connect(detailHref).get();

                //选择产品的5点描述
                Elements selectDetailLi = detailDoc.select("#featurebullets_feature_div").select("li");
                for (Element element : selectDetailLi) {
                    String txtDetail = element.select("span").select(".a-list-item").text();
                    System.err.println(txtDetail);
                }






                String description = product.select("div.a-section.a-spacing-none.a-spacing-top-small span.a-size-base").text();
                System.out.println("描述: " + description);

                // 假设评论在product中
                // 请根据实际的HTML结构调整选择器
                Elements reviews = product.select("span.a-size-base");
                for (Element review : reviews) {
                    System.out.println("评论: " + review.text());
                }
                System.out.println("-----------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

