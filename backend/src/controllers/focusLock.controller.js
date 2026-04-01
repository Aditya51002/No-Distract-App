const asyncHandler = require("../utils/asyncHandler");
const ApiResponse = require("../utils/apiResponse");
const focusLockService = require("../services/focusLock.service");
const User = require("../models/user.model");

// Blocked Apps
const addApp = asyncHandler(async (req, res) => {
    const app = await focusLockService.addBlockedApp(req.user._id, req.body);
    return res.status(201).json(new ApiResponse(201, app, "App blocked successfully"));
});

const getApps = asyncHandler(async (req, res) => {
    const apps = await focusLockService.getBlockedApps(req.user._id);
    return res.status(200).json(new ApiResponse(200, apps, "Apps fetched successfully"));
});

const deleteApp = asyncHandler(async (req, res) => {
    await focusLockService.deleteBlockedApp(req.user._id, req.params.id);
    return res.status(200).json(new ApiResponse(200, null, "App removed from block list"));
});

// Session Config
const getConfig = asyncHandler(async (req, res) => {
    const config = await focusLockService.getOrUpdateSessionConfig(req.user._id);
    return res.status(200).json(new ApiResponse(200, config, "Config fetched"));
});

const updateConfig = asyncHandler(async (req, res) => {
    const config = await focusLockService.getOrUpdateSessionConfig(req.user._id, req.body);
    return res.status(200).json(new ApiResponse(200, config, "Config updated"));
});

// Focus Sessions
const startSession = asyncHandler(async (req, res) => {
    const session = await focusLockService.startSession(req.user._id, req.body);
    return res.status(201).json(new ApiResponse(201, session, "Session started"));
});

const completeSession = asyncHandler(async (req, res) => {
    const { distractionAttempts } = req.body;
    const session = await focusLockService.completeSession(req.user._id, req.params.id, distractionAttempts);
    return res.status(200).json(new ApiResponse(200, session, "Session completed"));
});

// Analytics
const getSummary = asyncHandler(async (req, res) => {
    const summary = await focusLockService.getSummary(req.user._id);
    return res.status(200).json(new ApiResponse(200, summary, "Summary fetched"));
});

const getWeekly = asyncHandler(async (req, res) => {
    const stats = await focusLockService.getWeeklyStats(req.user._id);
    return res.status(200).json(new ApiResponse(200, stats, "Weekly stats fetched"));
});

const getHeatmap = asyncHandler(async (req, res) => {
    const data = await focusLockService.getHeatmapData(req.user._id);
    return res.status(200).json(new ApiResponse(200, data, "Heatmap data fetched"));
});

const getInsights = asyncHandler(async (req, res) => {
    const insights = focusLockService.generateInsights();
    return res.status(200).json(new ApiResponse(200, insights, "Insights fetched"));
});

// Gamification
const getRewards = asyncHandler(async (req, res) => {
    const user = await User.findById(req.user._id).select("xp level streak badges");
    return res.status(200).json(new ApiResponse(200, user, "Rewards fetched"));
});

// Challenges
const getChallenges = asyncHandler(async (req, res) => {
    const challenges = await focusLockService.getChallenges(req.user._id);
    return res.status(200).json(new ApiResponse(200, challenges, "Challenges fetched"));
});

// Reminders
const getReminders = asyncHandler(async (req, res) => {
    const reminders = await focusLockService.manageReminder(req.user._id, null); // Simplified
    return res.status(200).json(new ApiResponse(200, reminders, "Reminders fetched"));
});

const createReminder = asyncHandler(async (req, res) => {
    const reminder = await focusLockService.manageReminder(req.user._id, req.body);
    return res.status(201).json(new ApiResponse(201, reminder, "Reminder created"));
});

module.exports = {
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
};
