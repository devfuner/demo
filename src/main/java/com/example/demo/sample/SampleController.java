package com.example.demo.sample;

import com.example.demo.util.HtmlUnitUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sound.midi.Track;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/sample")
public class SampleController {

    final String SEARCH_URL = "https://m.search.naver.com/p/csearch/ocontent/util/headerjson.nhn?_callback=window.__jindo2_callback._7452&callapi=parceltracking&t_code=%s&t_invoice=%s&passportKey=772a914f386988c2d6a0249c85f35248c7a6337b";

    @Autowired
    HtmlUnitUtils htmlUnitUtils;

    @Autowired
    TrackingInfoMapper trackingInfoMapper;

    @Autowired
    TrackingHistoryMapper trackingHistoryMapper;

    public String htmlUnit() {
        // 로젠택배
        final String CODE = "06";

        // 운송장
        final String INVOICE = "90591925200";

        final String SEARCH_URL = "https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=택배+조회";
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage(SEARCH_URL);
            htmlUnitUtils.setCourier(page, CODE);
            htmlUnitUtils.setInvoice(page, INVOICE);
            htmlUnitUtils.click(page);
            htmlUnitUtils.getResult(page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /*public Map apiRequest(final String code, final String invoice) {
        final String url = String.format(SEARCH_URL, code, invoice);
        String data = "";
        try {
            data = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int startIndex = data.indexOf("(") + 1;
        int endIndex = data.indexOf(")");
        data = data.substring(startIndex, endIndex);


        Map<String, String> map = null;
        try {
            map = new ObjectMapper().readValue(data, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }*/

    private String getJsonData(String url) {
        String data = "";
        try {
            data = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int startIndex = data.indexOf("(") + 1;
        int endIndex = data.indexOf(")");

        return data.substring(startIndex, endIndex);
    }

    private TrackingInfo convertTrackingInfo(Map<String, Object> map) {
        TrackingInfo trackingInfo = new TrackingInfo();
        trackingInfo.setInvoiceNo((String) map.get("invoiceNo"));
        trackingInfo.setReceiverName((String) map.get("receiverName"));
        trackingInfo.setItemName((String) map.get("itemName"));
        trackingInfo.setProductInfo((String) map.get("productInfo"));
        trackingInfo.setSenderName((String) map.get("senderName"));
        trackingInfo.setReceiverAddr((String) map.get("receiverAddr"));
        trackingInfo.setOrderNumber((String) map.get("orderNumber"));

        return trackingInfo;
    }

    private List<TrackingHistory> convertTrackingHistory(List<Map<String, Object>> list, String invoiceNo) {
        List<TrackingHistory> result = new ArrayList<>();
        for (Map m : list) {
            TrackingHistory trackingHistory = new TrackingHistory();

            trackingHistory.setInvoiceNo(invoiceNo);
            trackingHistory.setCurrentLocation((String) m.get("where"));
            trackingHistory.setTrackingStatus((String) m.get("kind"));
            trackingHistory.setTelNo((String) m.get("telno"));
            trackingHistory.setTimeString((String) m.get("timeString"));

            result.add(trackingHistory);
        }

        return result;
    }

    private void dbProcess(final String jsonStr) {
        try {
            Map<String, Object> mapTrackingInfo = new ObjectMapper().readValue(jsonStr, HashMap.class);
            TrackingInfo trackingInfo = convertTrackingInfo(mapTrackingInfo);
            TrackingInfo has = trackingInfoMapper.getById(trackingInfo);

            if (has == null) {
                trackingInfoMapper.insert(trackingInfo);
            }

            List<Map<String, Object>> listTrackingHistory = (List<Map<String, Object>>) mapTrackingInfo.get("trackingDetails");
            List<TrackingHistory> historyList = convertTrackingHistory(listTrackingHistory, trackingInfo.getInvoiceNo());

            trackingHistoryMapper.delete(trackingInfo.getInvoiceNo());

            for (TrackingHistory trackingHistory : historyList) {
                trackingHistoryMapper.insert(trackingHistory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String apiRequest(
            @RequestParam final String courier,
            @RequestParam final String invoice) {
        final String url = String.format(SEARCH_URL, courier, invoice);
        String jsonStr = getJsonData(url);

        dbProcess(jsonStr);

        return jsonStr;
    }

    public LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime
                .ofInstant(
                        Instant.ofEpochMilli(timestamp),
                        TimeZone.getDefault().toZoneId());
    }

    @GetMapping("/view")
    public String get(Model model) throws Exception {
        return "sample/view";
    }
}
