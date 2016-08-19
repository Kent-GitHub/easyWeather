package com.easynetwork.weather.core.menu.menu_right;

import android.graphics.Color;

import com.easynetwork.weather.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanming on 2016/8/16.
 */
public class CodeToValues {

    private static final Map<Integer, Integer> imageValues = new HashMap<>();
    private static final Map<Integer, Integer> iconValues = new HashMap<>();
    private static final Map<Integer, String> colorValues = new HashMap<>();

    static {
        imageValues.put(100, R.drawable.dcr_qlsy_normal);
        imageValues.put(101, R.drawable.dcr_qjdy_normal);
        imageValues.put(102, R.drawable.dcr_qlsy_normal);
        imageValues.put(103, R.drawable.dcr_qjdy_normal);
        imageValues.put(104, R.drawable.dcr_yymb_normal);
        imageValues.put(205, R.drawable.dcr_dflx_normal);
        imageValues.put(206, R.drawable.dcr_dflx_normal);
        imageValues.put(207, R.drawable.dcr_dflx_normal);
        imageValues.put(208, R.drawable.dcr_tflx_normal);
        imageValues.put(209, R.drawable.dcr_tflx_normal);
        imageValues.put(210, R.drawable.dcr_tflx_normal);
        imageValues.put(211, R.drawable.dcr_tflx_normal);
        imageValues.put(212, R.drawable.dcr_tflx_normal);
        imageValues.put(213, R.drawable.dcr_tflx_normal);
        imageValues.put(300, R.drawable.dcr_oyzy_normal);
        imageValues.put(301, R.drawable.dcr_oyzy_normal);
        imageValues.put(302, R.drawable.dcr_dsly_normal);
        imageValues.put(303, R.drawable.dcr_dsly_normal);
        imageValues.put(304, R.drawable.dcr_zybb_normal);
        imageValues.put(305, R.drawable.dcr_oyzy_normal);
        imageValues.put(306, R.drawable.dcr_zyjl_normal);
        imageValues.put(307, R.drawable.dcr_dyqp_normal);
        imageValues.put(308, R.drawable.dcr_bylx_normal);
        imageValues.put(309, R.drawable.dcr_xymm_normal);
        imageValues.put(310, R.drawable.dcr_bylx_normal);
        imageValues.put(311, R.drawable.dcr_bylx_normal);
        imageValues.put(312, R.drawable.dcr_bylx_normal);
        imageValues.put(313, R.drawable.dcr_dyjl_normal);
        imageValues.put(400, R.drawable.dcr_lxxx_normal);
        imageValues.put(401, R.drawable.dcr_xhff_normal);
        imageValues.put(402, R.drawable.dcr_mtbx_normal);
        imageValues.put(403, R.drawable.dcr_bxlx_normal);
        imageValues.put(404, R.drawable.dcr_oyjx_normal);
        imageValues.put(405, R.drawable.dcr_oyjx_normal);
        imageValues.put(406, R.drawable.dcr_oyjx_normal);
        imageValues.put(407, R.drawable.dcr_oyjx_normal);
        imageValues.put(500, R.drawable.dcr_wqlz_normal);
        imageValues.put(501, R.drawable.dcr_wqlz_normal);
        imageValues.put(502, R.drawable.dcr_qdwr_normal);
        imageValues.put(503, R.drawable.dcr_fcfy_normal);
        imageValues.put(504, R.drawable.dcr_fcfy_normal);
        imageValues.put(506, R.drawable.dcr_scxj_normal);
        imageValues.put(507, R.drawable.dcr_scxj_normal);
        imageValues.put(508, R.drawable.dcr_scxj_normal);
        imageValues.put(900, R.drawable.dcr_jrgw_normal);
        imageValues.put(901, R.drawable.dcr_wdzj_normal);
    }

    static {
        iconValues.put(100, R.drawable.icon_qing);
        iconValues.put(101, R.drawable.icon_dy);
        iconValues.put(102, R.drawable.icon_dy);
        iconValues.put(103, R.drawable.icon_dy);
        iconValues.put(104, R.drawable.icon_yin);
        iconValues.put(205, R.drawable.icon_na);
        iconValues.put(206, R.drawable.icon_na);
        iconValues.put(207, R.drawable.icon_na);
        iconValues.put(208, R.drawable.icon_na);
        iconValues.put(209, R.drawable.icon_na);
        iconValues.put(210, R.drawable.icon_na);
        iconValues.put(211, R.drawable.icon_na);
        iconValues.put(212, R.drawable.icon_na);
        iconValues.put(213, R.drawable.icon_na);
        iconValues.put(300, R.drawable.icon_zy);
        iconValues.put(301, R.drawable.icon_zy);
        iconValues.put(302, R.drawable.icon_lzy);
        iconValues.put(303, R.drawable.icon_lzy);
        iconValues.put(304, R.drawable.icon_lzy);
        iconValues.put(305, R.drawable.icon_xdzy);
        iconValues.put(306, R.drawable.icon_zddy);
        iconValues.put(307, R.drawable.icon_ddby);
        iconValues.put(308, R.drawable.icon_tdby);
        iconValues.put(309, R.drawable.icon_xdzy);
        iconValues.put(310, R.drawable.icon_dby);
        iconValues.put(311, R.drawable.icon_dby);
        iconValues.put(312, R.drawable.icon_tdby);
        iconValues.put(313, R.drawable.icon_zddy);
        iconValues.put(400, R.drawable.icon_zx);
        iconValues.put(401, R.drawable.icon_zx);
        iconValues.put(402, R.drawable.icon_dx);
        iconValues.put(403, R.drawable.icon_bx);
        iconValues.put(404, R.drawable.icon_dx);
        iconValues.put(405, R.drawable.icon_dx);
        iconValues.put(406, R.drawable.icon_dx);
        iconValues.put(407, R.drawable.icon_dx);
        iconValues.put(500, R.drawable.icon_wu);
        iconValues.put(501, R.drawable.icon_wu);
        iconValues.put(502, R.drawable.icon_m);
        iconValues.put(503, R.drawable.icon_qsb);
        iconValues.put(504, R.drawable.icon_qsb);
        iconValues.put(506, R.drawable.icon_qsb);
        iconValues.put(507, R.drawable.icon_qsb);
        iconValues.put(508, R.drawable.icon_qsb);
        iconValues.put(900, R.drawable.icon_na);
        iconValues.put(901, R.drawable.icon_na);
    }

    static {

        colorValues.put(100, "e42640");//晴//e42640//红
        colorValues.put(101, "e42640");//多云//
        colorValues.put(102, "e42640");//少云//
        colorValues.put(103, "e42640");//晴间多云//
        colorValues.put(104, "e42640");//阴//

        colorValues.put(200, "2684e4");//有风//浅蓝　2684e4 -
        colorValues.put(201, "2684e4");//平静//
        colorValues.put(202, "2684e4");//微风//
        colorValues.put(203, "2684e4");//和风//
        colorValues.put(204, "2684e4");//清风//
        colorValues.put(205, "2684e4");//强风/劲风//
        colorValues.put(206, "2684e4");//疾风//
        colorValues.put(207, "2684e4");//大风//
        colorValues.put(208, "2684e4");//烈风//
        colorValues.put(209, "b23818");//风暴//赭石色  b23818
        colorValues.put(210, "b23818");//狂爆风//
        colorValues.put(211, "b23818");//飓风//
        colorValues.put(212, "b23818");//龙卷风//
        colorValues.put(213, "b23818");//热带风暴//

        colorValues.put(300, "3a25d6");//阵雨//蓝色　3a25d6
        colorValues.put(301, "3a25d6");//强阵雨//
        colorValues.put(302, "3a25d6");//雷阵雨//  橄榄绿　159959
        colorValues.put(303, "159959");//强雷阵雨//
        colorValues.put(304, "159959");//雷阵雨伴有冰雹//
        colorValues.put(305, "3a25d6");//小雨//
        colorValues.put(306, "3a25d6");//中雨//
        colorValues.put(307, "3a25d6");//大雨//
        colorValues.put(308, "3a25d6");//极端降雨//
        colorValues.put(309, "3a25d6");//毛毛雨/细雨//
        colorValues.put(310, "159959");//暴雨//
        colorValues.put(311, "159959");//大暴雨//
        colorValues.put(312, "159959");//特大暴雨//
        colorValues.put(313, "3a25d6");//冻雨//

        colorValues.put(400, "159997");//小雪//
        colorValues.put(401, "159997");//中雪//湖蓝　159997
        colorValues.put(402, "159997");//大雪//
        colorValues.put(403, "159997");//暴雪//
        colorValues.put(404, "159997");//雨夹雪//
        colorValues.put(405, "159997");//雨雪天气//
        colorValues.put(406, "159997");//阵雨夹雪//
        colorValues.put(407, "159997");//阵雪//

        colorValues.put(500, "2684e4");//薄雾//浅蓝　2684e4 -
        colorValues.put(501, "2684e4");//雾//
        colorValues.put(502, "159959");//霾//橄榄绿　159959
        colorValues.put(503, "b07211");//扬沙//土黄  b07211
        colorValues.put(504, "b07211");//浮尘//
        colorValues.put(506, "b07211");//火山灰//
        colorValues.put(507, "b07211");//沙尘暴//
        colorValues.put(508, "b07211");//强沙尘暴//

        colorValues.put(900, "cc25d6");//热//紫　cc25d6　
        colorValues.put(901, "b23818");//冷//草绿　16a023
    }

    private static final String[] colors = new String[]{"#e42640", "#cc25d6", "#3a25d6",
            "#2684e4", "#159997", "#159959", "#16a023", "#b07211", "#b23818"};

    public static int getRandomColor() {
        return Color.parseColor(colors[(int) (Math.random() * colors.length)]);
    }

    public static int getColorByCode(int weatherCode) {
        return Color.parseColor("#" + colorValues.get(weatherCode));
    }

    public static int getImageDescribe(int weatherCode) {
        Integer result = imageValues.get(weatherCode);
        if (result <= 0) {
            result = R.drawable.dcr_weather_na;
        }
        return result;
    }

    public static int getIconDescribe(int weatherCode) {
        Integer result = iconValues.get(weatherCode);
        if (result <= 0) {
            result = R.drawable.icon_na;
        }
        return result;
    }
}
