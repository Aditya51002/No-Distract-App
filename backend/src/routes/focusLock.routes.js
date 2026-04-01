const { Router } = require("express");
const {
    addApp,
    getApps,
    deleteApp,
    getConfig,
    updateConfig,
    startSession,
    completeSession,
    getSummary,
    getWeekly,
    getHeatmap,
    getInsights,
    getRewards,
    getChallenges,
    getReminders,
    createReminder
} = require("../controllers/focusLock.controller");
const verifyJWT = require("../middlewares/auth.middleware");

const router = Router();

// All routes are protected
router.use(verifyJWT);

// Blocked Apps
router.route("/apps").post(addApp).get(getApps);
router.route("/apps/:id").delete(deleteApp);

// Session Config
router.route("/session-config").get(getConfig).put(updateConfig);

// Focus Sessions
router.route("/sessions").post(startSession);
router.route("/sessions/:id/complete").patch(completeSession);

// Analytics
router.route("/analytics/summary").get(getSummary);
router.route("/analytics/weekly").get(getWeekly);
router.route("/analytics/heatmap").get(getHeatmap);
router.route("/analytics/insights").get(getInsights);

// Gamification & Rewards
router.route("/rewards").get(getRewards);
router.route("/challenges").get(getChallenges);

// Reminders
router.route("/reminders").get(getReminders).post(createReminder);

module.exports = router;
