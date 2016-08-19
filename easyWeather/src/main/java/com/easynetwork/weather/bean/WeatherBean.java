package com.easynetwork.weather.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherBean {

    private static final int MAX_DAY = 3;

    /**
     * basic : {"city":"杭州","cnty":"中国","update":"2016-08-18 14:30"}
     * alarms : empty
     * aqi : {"qlty":"优","pm25":"22","aqi":"48"}
     * daily_forecast : [{"date":"今天8月18日","astro":{"sr":"05:17","ss":"18:44"},"tmp":{"min":"28","max":"37"},"cond":{"code":"100","day":"高温天气 注意防暑","abstract":"晴"}},{"date":"2016-8-19","astro":{"sr":"05:17","ss":"18:44"},"tmp":{"min":"30","max":"38"},"cond":{"code":"100","day":"晴","abstract":"晴"}},{"date":"2016-8-20","astro":{"sr":"05:17","ss":"18:43"},"tmp":{"min":"30","max":"37"},"cond":{"code":"101","day":"多云","abstract":"多云"}}]
     * now : {"cond":{"code":"100","txt":"晴"},"tmp":"36"}
     * highlow : 和昨天气温相当
     * timestamp : 1471514994
     */

    private DataBean data;
    /**
     * data : {"basic":{"city":"杭州","cnty":"中国","update":"2016-08-18 14:30"},"alarms":"empty","aqi":{"qlty":"优","pm25":"22","aqi":"48"},"daily_forecast":[{"date":"今天8月18日","astro":{"sr":"05:17","ss":"18:44"},"tmp":{"min":"28","max":"37"},"cond":{"code":"100","day":"高温天气 注意防暑","abstract":"晴"}},{"date":"2016-8-19","astro":{"sr":"05:17","ss":"18:44"},"tmp":{"min":"30","max":"38"},"cond":{"code":"100","day":"晴","abstract":"晴"}},{"date":"2016-8-20","astro":{"sr":"05:17","ss":"18:43"},"tmp":{"min":"30","max":"37"},"cond":{"code":"101","day":"多云","abstract":"多云"}}],"now":{"cond":{"code":"100","txt":"晴"},"tmp":"36"},"highlow":"和昨天气温相当","timestamp":"1471514994"}
     * errNum : 200
     * errMsg : Success
     */

    private String errNum;
    private String errMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getErrNum() {
        return errNum;
    }

    public void setErrNum(String errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public static class DataBean {
        /**
         * city : 杭州
         * cnty : 中国
         * update : 2016-08-18 14:30
         */

        private BasicBean basic;
        private String alarms;
        /**
         * qlty : 优
         * pm25 : 22
         * aqi : 48
         */

        private AqiBean aqi;
        /**
         * cond : {"code":"100","txt":"晴"}
         * tmp : 36
         */

        private NowBean now;
        private String highlow;
        private String timestamp;
        /**
         * date : 今天8月18日
         * astro : {"sr":"05:17","ss":"18:44"}
         * tmp : {"min":"28","max":"37"}
         * cond : {"code":"100","day":"高温天气 注意防暑","abstract":"晴"}
         */

        private List<DailyForecastBean> daily_forecast;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public String getAlarms() {
            return alarms;
        }

        public void setAlarms(String alarms) {
            this.alarms = alarms;
        }

        public AqiBean getAqi() {
            return aqi;
        }

        public void setAqi(AqiBean aqi) {
            this.aqi = aqi;
        }

        public NowBean getNow() {
            return now;
        }

        public void setNow(NowBean now) {
            this.now = now;
        }

        public String getHighlow() {
            return highlow;
        }

        public void setHighlow(String highlow) {
            this.highlow = highlow;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public List<DailyForecastBean> getDaily_forecast() {
            return daily_forecast;
        }

        public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public static class BasicBean {
            private String city;
            private String cnty;
            private String update;

            /**
             * city : 杭州
             * cnty : 中国
             * update : 2016-08-18 14:30
             */
            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getUpdate() {
                return update;
            }

            public void setUpdate(String update) {
                this.update = update;
            }
        }

        public static class AqiBean {
            private String qlty;
            private String pm25;
            private String aqi;

            /**
             * qlty : 优
             * pm25 : 22
             * aqi : 48
             */
            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }
        }

        public static class NowBean {
            /**
             * code : 100
             * txt : 晴
             */

            private CondBean cond;
            private String tmp;

            /**
             * cond : {"code":"100","txt":"晴"}
             * tmp : 36
             */
            public CondBean getCond() {
                return cond;
            }

            public void setCond(CondBean cond) {
                this.cond = cond;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public static class CondBean {
                private String code;
                private String txt;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }
        }

        public static class DailyForecastBean {
            /**
             * date : 今天8月18日
             * astro : {"sr":"05:17","ss":"18:44"}
             * tmp : {"min":"28","max":"37"}
             * cond : {"code":"100","day":"高温天气 注意防暑","abstract":"晴"}
             */
            private String date;
            /**
             * sr : 05:17
             * ss : 18:44
             */

            private AstroBean astro;
            /**
             * min : 28
             * max : 37
             */

            private TmpBean tmp;
            /**
             * code : 100
             * day : 高温天气 注意防暑
             * abstract : 晴
             */

            private CondBean cond;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public AstroBean getAstro() {
                return astro;
            }

            public void setAstro(AstroBean astro) {
                this.astro = astro;
            }

            public TmpBean getTmp() {
                return tmp;
            }

            public void setTmp(TmpBean tmp) {
                this.tmp = tmp;
            }

            public CondBean getCond() {
                return cond;
            }

            public void setCond(CondBean cond) {
                this.cond = cond;
            }

            public static class AstroBean {
                private String sr;
                private String ss;

                public String getSr() {
                    return sr;
                }

                public void setSr(String sr) {
                    this.sr = sr;
                }

                public String getSs() {
                    return ss;
                }

                public void setSs(String ss) {
                    this.ss = ss;
                }
            }

            public static class TmpBean {
                private String min;
                private String max;

                public String getMin() {
                    return min;
                }

                public void setMin(String min) {
                    this.min = min;
                }

                public String getMax() {
                    return max;
                }

                public void setMax(String max) {
                    this.max = max;
                }
            }

            public static class CondBean {
                private String code;
                private String day;
                private String abstractDescribe;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public String getAbstractDescribe() {
                    return abstractDescribe;
                }

                public void setAbstractDescribe(String abstractX) {
                    this.abstractDescribe = abstractX;
                }
            }
        }
    }


    //----------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------
    //为SimpleWeatherData提供方法

    public Date getDate() {
        String time = getData().getBasic().getUpdate();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getCity() {
        return getData().getBasic().getCity();
    }

    public String getRtCode() {
        return getData().getNow().getCond().getCode();
    }

    public String getDayCode(int day) {
        if (day > MAX_DAY - 1) return null;
        return getData().getDaily_forecast().get(day).getCond().getCode();
    }

    public String getRtDescribe() {
        return getData().getNow().getCond().getTxt();
    }

    public String getDayDescribe(int day) {
        if (day > MAX_DAY - 1) return null;
        return getData().getDaily_forecast().get(day).getCond().getDay();
    }

    public String getRtTmp() {
        return getData().getNow().getTmp();
    }

    public String getTmpRange(int day) {
        if (day > MAX_DAY - 1) return null;
        String max = getData().getDaily_forecast().get(day).getTmp().getMax();
        String min = getData().getDaily_forecast().get(day).getTmp().getMin();
        return max + "~" + min + "°C";
    }

    public long getTimeStamp() {
        String time = getData().getBasic().getUpdate();
        long timeStamp = -1;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
            timeStamp = date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public DailyWeatherData getDailyDate(int day) {
        if (day > MAX_DAY - 1) return null;
        DailyWeatherData data = new DailyWeatherData();
        DataBean.DailyForecastBean dailyBean = getData().getDaily_forecast().get(day);
        if (day == 0) {
            data.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-0", "-");
        } else {
            data.date = dailyBean.getDate();
        }
        data.txt = dailyBean.getCond().getDay();
        data.code = dailyBean.getCond().getCode();
        data.describe = dailyBean.getCond().getAbstractDescribe();
        data.minTmp = dailyBean.getTmp().getMin();
        data.maxTmp = dailyBean.getTmp().getMax();
        return data;
    }
}
