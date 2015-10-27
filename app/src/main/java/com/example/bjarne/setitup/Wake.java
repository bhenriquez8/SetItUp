package com.example.bjarne.setitup;

/**
 * Created by bjarne on 10/15/15.
 */
public class Wake {
        private String wakeTime;
        private String setMessage;

        public Wake() {}
        public Wake(String setMessage, String wakeTime) {
            this.wakeTime = wakeTime;
            this.setMessage = setMessage;
        }

        public String getWakeTime() {
            return wakeTime;
        }

        public void setWakeTime(String wakeTime) {
            this.wakeTime = wakeTime;
        }

        public String getSetMessage() {
            return setMessage;
        }

        public void setSetMessage(String setMessage) {
            this.setMessage = setMessage;
        }

        public String toString() {
            return this.wakeTime;
        }
}
