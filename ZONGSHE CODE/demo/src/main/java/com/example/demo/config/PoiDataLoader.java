package com.example.demo.config;

import com.example.demo.entity.Poi;
import com.example.demo.repository.PoiRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PoiDataLoader implements CommandLineRunner {

    private final PoiRepository poiRepository;

    // 
    private static final String AMAP_KEY = "561e56e79c9a57a4dc7e860b299784a4";

    // 目标数量
    private static final int TARGET_COUNT = 500;

    @Override
    public void run(String... args) throws Exception {
        long existing = poiRepository.count();
        if (existing >= TARGET_COUNT) {
            System.out.println("POI 数量已满足 (" + existing + ")，跳过加载");
            return;
        }

        System.out.println("开始加载 POI，目标数量：" + TARGET_COUNT);

        // 关键词列表，覆盖常见类型
        String[] keywords = {
            "餐厅", "酒店", "医院", "学校", "商场", "公园",
            "银行", "加油站", "地铁站", "停车场",
            "超市", "药店", "咖啡馆", "写字楼", "体育场馆",
            "电影院", "景点", "公交站", "派出所", "便利店"
        };

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        int total = 0;

        for (String keyword : keywords) {
            if (total >= TARGET_COUNT) break;

            // 每个关键词翻 5 页（每页 20 条）
            for (int page = 1; page <= 5; page++) {
                if (total >= TARGET_COUNT) break;

                String url = String.format(
                    "https://restapi.amap.com/v3/place/text?key=%s&keywords=%s&city=成都&citylimit=true&offset=20&page=%d&extensions=base",
                    AMAP_KEY, keyword, page
                );

                try {
                    String json = restTemplate.getForObject(url, String.class);
                    JsonNode root = mapper.readTree(json);

                    // 检查状态
                    if (!"1".equals(root.get("status").asText())) {
                        System.err.println("API 返回错误：" + root.get("info").asText());
                        break;
                    }

                    JsonNode pois = root.get("pois");
                    if (pois == null || pois.size() == 0) break;

                    for (JsonNode p : pois) {
                        if (total >= TARGET_COUNT) break;

                        String name = p.get("name").asText();
                        String address = p.has("address") ? p.get("address").asText() : "";
                        String location = p.has("location") ? p.get("location").asText() : "";

                        // 有些 POI 没有坐标，跳过
                        if (location.isEmpty() || !location.contains(",")) continue;

                        String[] parts = location.split(",");
                        if (parts.length != 2) continue;

                        // 高德返回格式："经度,纬度"
                        double lng = Double.parseDouble(parts[0]);
                        double lat = Double.parseDouble(parts[1]);

                        Poi poi = new Poi();
                        poi.setPoiName(name);
                        poi.setPoiType(keyword);
                        poi.setPoiAddress(address);
                        poi.setPoiLng(lng);
                        poi.setPoiLat(lat);

                        poiRepository.save(poi);
                        total++;

                        if (total % 50 == 0) {
                            System.out.println("已加载 " + total + " 个 POI...");
                        }
                    }

                    Thread.sleep(200);  // 防止触发频率限制

                } catch (Exception e) {
                    System.err.println("请求失败：" + e.getMessage());
                }
            }
        }

        System.out.println("✅ POI 加载完成，总数：" + total);
    }
}