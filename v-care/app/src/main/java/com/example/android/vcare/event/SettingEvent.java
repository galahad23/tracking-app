package com.example.android.vcare.event;

import com.example.android.vcare.model.Faq;

import java.util.List;

public class SettingEvent {

    public static class OnGetFaq {
        private List<Faq> faqList;
        private int hashCode;

        public OnGetFaq(List<Faq> faqList, int hashCode) {
            this.faqList = faqList;
            this.hashCode = hashCode;
        }

        public List<Faq> getFaqList() {
            return faqList;
        }

        public int getHashCode() {
            return hashCode;
        }
    }
}
