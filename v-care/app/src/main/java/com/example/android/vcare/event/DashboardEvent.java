package com.example.android.vcare.event;

import com.example.android.vcare.model.DashboardResult;

public class DashboardEvent {

    public static class OnGetDashboard {
        private DashboardResult result;
        private int hashCode;

        public OnGetDashboard(DashboardResult result, int hashCode) {
            this.result = result;
            this.hashCode = hashCode;
        }

        public DashboardResult getResult() {
            return result;
        }

        public int getHashCode() {
            return hashCode;
        }
    }
}
