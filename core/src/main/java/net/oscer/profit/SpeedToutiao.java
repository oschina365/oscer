package net.oscer.profit;

/**
 * @author 极速版头条签到后返回的对象
 * @create 2020-04-26 14:57
 **/
public class SpeedToutiao {


    /**
     * data : {"current_time":1587883051,"double_bonus":false,"new_excitation_ad":{"ad_id":2,"fixed":false,"score_amount":31,"score_source":1,"task_id":188},"next_treasure_time":1587883651,"pre_ui_status":2,"score_amount":1300,"share_amount":200,"show_watch_video_task":false,"stop_ts":1588217626,"treasure_ui_status":2}
     * err_no : 0
     * err_tips : success
     */
    /**
     * data:{}
     * err_no : 1026
     * err_tips : 开宝箱时间错误
     * {"err_no": 1026, "data": {}, "err_tips": "\u5f00\u5b9d\u7bb1\u65f6\u95f4\u9519\u8bef"}
     */

    private DataBean data;
    private int err_no;
    private String err_tips;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErr_no() {
        return err_no;
    }

    public void setErr_no(int err_no) {
        this.err_no = err_no;
    }

    public String getErr_tips() {
        return err_tips;
    }

    public void setErr_tips(String err_tips) {
        this.err_tips = err_tips;
    }

    public static class DataBean {
        /**
         * current_time : 1587883051
         * double_bonus : false
         * new_excitation_ad : {"ad_id":2,"fixed":false,"score_amount":31,"score_source":1,"task_id":188}
         * next_treasure_time : 1587883651
         * pre_ui_status : 2
         * score_amount : 1300
         * share_amount : 200
         * show_watch_video_task : false
         * stop_ts : 1588217626
         * treasure_ui_status : 2
         */
        //当前签到时间
        private long current_time;
        //是否双倍奖励
        private boolean double_bonus;
        //激励，看广告
        private NewExcitationAdBean new_excitation_ad;
        //下一个开启宝藏时间
        private long next_treasure_time;
        //预用户状态
        private int pre_ui_status;
        //得到的金币数量
        private int score_amount;
        private int share_amount;
        private boolean show_watch_video_task;
        private int stop_ts;
        private int treasure_ui_status;

        public long getCurrent_time() {
            return current_time;
        }

        public void setCurrent_time(long current_time) {
            this.current_time = current_time;
        }

        public boolean isDouble_bonus() {
            return double_bonus;
        }

        public void setDouble_bonus(boolean double_bonus) {
            this.double_bonus = double_bonus;
        }

        public NewExcitationAdBean getNew_excitation_ad() {
            return new_excitation_ad;
        }

        public void setNew_excitation_ad(NewExcitationAdBean new_excitation_ad) {
            this.new_excitation_ad = new_excitation_ad;
        }

        public long getNext_treasure_time() {
            return next_treasure_time;
        }

        public void setNext_treasure_time(long next_treasure_time) {
            this.next_treasure_time = next_treasure_time;
        }

        public int getPre_ui_status() {
            return pre_ui_status;
        }

        public void setPre_ui_status(int pre_ui_status) {
            this.pre_ui_status = pre_ui_status;
        }

        public int getScore_amount() {
            return score_amount;
        }

        public void setScore_amount(int score_amount) {
            this.score_amount = score_amount;
        }

        public int getShare_amount() {
            return share_amount;
        }

        public void setShare_amount(int share_amount) {
            this.share_amount = share_amount;
        }

        public boolean isShow_watch_video_task() {
            return show_watch_video_task;
        }

        public void setShow_watch_video_task(boolean show_watch_video_task) {
            this.show_watch_video_task = show_watch_video_task;
        }

        public int getStop_ts() {
            return stop_ts;
        }

        public void setStop_ts(int stop_ts) {
            this.stop_ts = stop_ts;
        }

        public int getTreasure_ui_status() {
            return treasure_ui_status;
        }

        public void setTreasure_ui_status(int treasure_ui_status) {
            this.treasure_ui_status = treasure_ui_status;
        }

        public static class NewExcitationAdBean {
            /**
             * ad_id : 2
             * fixed : false
             * score_amount : 31
             * score_source : 1
             * task_id : 188
             */

            private int ad_id;
            private boolean fixed;
            private int score_amount;
            private int score_source;
            private int task_id;

            public int getAd_id() {
                return ad_id;
            }

            public void setAd_id(int ad_id) {
                this.ad_id = ad_id;
            }

            public boolean isFixed() {
                return fixed;
            }

            public void setFixed(boolean fixed) {
                this.fixed = fixed;
            }

            public int getScore_amount() {
                return score_amount;
            }

            public void setScore_amount(int score_amount) {
                this.score_amount = score_amount;
            }

            public int getScore_source() {
                return score_source;
            }

            public void setScore_source(int score_source) {
                this.score_source = score_source;
            }

            public int getTask_id() {
                return task_id;
            }

            public void setTask_id(int task_id) {
                this.task_id = task_id;
            }
        }
    }
}
